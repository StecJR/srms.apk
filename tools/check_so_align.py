#!/usr/bin/env python3
"""
Check PT_LOAD p_align for .so files to ensure 16KB page size (p_align == 0x4000)
Usage:
  python tools/check_so_align.py --apk path/to/app.apk
  python tools/check_so_align.py --aar path/to/lib.aar
  python tools/check_so_align.py --dir path/to/dir
  python tools/check_so_align.py --scan path/to/scan-root

Exit code 0 when all .so files have p_align == 0x4000, otherwise non-zero and prints offending files.
Requires: pyelftools (pip install pyelftools)
"""
import argparse
import os
import sys
import zipfile
import tempfile
import shutil
from elftools.elf.elffile import ELFFile

TARGET_ALIGN = 0x4000


def check_so_file(path):
    try:
        with open(path, 'rb') as f:
            elf = ELFFile(f)
            for seg in elf.iter_segments():
                if seg['p_type'] == 'PT_LOAD':
                    align = seg['p_align']
                    if align != TARGET_ALIGN:
                        print(f"{path}: PT_LOAD p_align=0x{align:x} != 0x{TARGET_ALIGN:x}")
                        return False
    except Exception as e:
        print(f"{path}: ERROR parsing ELF: {e}")
        return False
    return True


def extract_and_check_archive(archive_path, inner_dir_patterns=('lib/', 'jni/')):
    tmp = tempfile.mkdtemp(prefix='checkso_')
    bad = False
    try:
        with zipfile.ZipFile(archive_path, 'r') as z:
            z.extractall(tmp)
        # search for .so under expected dirs or anywhere
        for root, dirs, files in os.walk(tmp):
            for name in files:
                if name.endswith('.so'):
                    full = os.path.join(root, name)
                    if not check_so_file(full):
                        bad = True
    finally:
        shutil.rmtree(tmp)
    return not bad


def scan_dir(root):
    bad_found = False
    for dirpath, dirnames, filenames in os.walk(root):
        for f in filenames:
            lp = os.path.join(dirpath, f)
            if f.endswith('.so'):
                if not check_so_file(lp):
                    bad_found = True
            elif f.endswith('.apk') or f.endswith('.aar') or f.endswith('.zip'):
                if not extract_and_check_archive(lp):
                    bad_found = True
    return not bad_found


def main():
    p = argparse.ArgumentParser()
    p.add_argument('--apk', help='Path to an APK to inspect')
    p.add_argument('--aar', help='Path to an AAR to inspect')
    p.add_argument('--dir', help='Path to a directory containing .so files')
    p.add_argument('--scan', help='Recursively scan a directory for .so/apk/aar and check them')
    args = p.parse_args()

    ok = True
    if args.apk:
        if not os.path.exists(args.apk):
            print(f"APK not found: {args.apk}")
            sys.exit(2)
        ok = extract_and_check_archive(args.apk)
    elif args.aar:
        if not os.path.exists(args.aar):
            print(f"AAR not found: {args.aar}")
            sys.exit(2)
        ok = extract_and_check_archive(args.aar)
    elif args.dir:
        if not os.path.exists(args.dir):
            print(f"Directory not found: {args.dir}")
            sys.exit(2)
        ok = scan_dir(args.dir)
    elif args.scan:
        if not os.path.exists(args.scan):
            print(f"Path not found: {args.scan}")
            sys.exit(2)
        ok = scan_dir(args.scan)
    else:
        p.print_help()
        sys.exit(2)

    if ok:
        print('OK: all checked ELF .so files have PT_LOAD p_align == 0x4000')
        sys.exit(0)
    else:
        print('FAIL: one or more .so files have incorrect p_align (see above)')
        sys.exit(3)


if __name__ == '__main__':
    main()
