package com.stec.srms.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.stec.srms.R;
import com.stec.srms.database.StudentDBHandler;
import com.stec.srms.model.MarkSheetData;
import com.stec.srms.model.StudentInfo;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

class FooterEvent extends PdfPageEventHelper {
    private final Font footerFont;

    public FooterEvent() {
        this.footerFont = new Font(Font.COURIER, 10, Font.NORMAL, new Color(80, 100, 150, 150));
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        // Left footer
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_LEFT,
                new Phrase("Generated from SRMS app", footerFont),
                document.leftMargin(),
                document.bottomMargin() - 10,
                0
        );
        // Right footer
        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_RIGHT,
                new Phrase("© 2025 · Jakir Hossain", footerFont),
                document.getPageSize().getWidth() - document.rightMargin(),
                document.bottomMargin() - 10,
                0
        );
    }
}

public class Util {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static Drawable eyeShow, eyeHide;

    public static double getGpa(int mark) {
        if (mark >= 80) return 4.0;
        else if (mark >= 75) return 3.75;
        else if (mark >= 70) return 3.5;
        else if (mark >= 65) return 3.25;
        else if (mark >= 60) return 3.0;
        else if (mark >= 55) return 2.75;
        else if (mark >= 50) return 2.5;
        else if (mark >= 45) return 2.25;
        else if (mark >= 40) return 2.0;
        else return 0.0;
    }
    public static String getGrade(double gpa) {
        if (gpa >= 4.0) return "A+";
        else if (gpa >= 3.75) return "A";
        else if (gpa >= 3.5) return "A-";
        else if (gpa >= 3.25) return "B+";
        else if (gpa >= 3.0) return "B";
        else if (gpa >= 2.75) return "B-";
        else if (gpa >= 2.5) return "C+";
        else if (gpa >= 2.25) return "C";
        else if (gpa >= 2.0) return "C-";
        else return "F";
    }
    public static int stringToInt(String value, int defaultValue) {
        if (value == null || value.trim().isBlank()) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public static int dpToPx(@NonNull Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    // Password show/hide
    private static void setCompoundDrawable(EditText editText, Drawable drawable) {
        editText.setCompoundDrawablesWithIntrinsicBounds(
                editText.getCompoundDrawables()[0],
                editText.getCompoundDrawables()[1],
                drawable,
                editText.getCompoundDrawables()[3]
        );
    }
    private static boolean isTouchWithinDrawableBounds(EditText editText, MotionEvent event) {
        Drawable drawable = editText.getCompoundDrawables()[2];
        return drawable != null && event.getRawX() >= (editText.getRight() - drawable.getBounds().width() - 50);
    }
    private static void togglePasswordVisibility(EditText editText) {
        Typeface typeFace = editText.getTypeface();
        int cursorPosition = editText.getSelectionStart();
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            setCompoundDrawable(editText, eyeShow);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setCompoundDrawable(editText, eyeHide);
        }
        editText.setSelection(cursorPosition);
        editText.setTypeface(typeFace);
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void addPasswordVisibilityToggler(@NonNull EditText editText, @NonNull Context context) {
        if (eyeShow == null || eyeHide == null) {
            eyeShow = ContextCompat.getDrawable(context, R.drawable.eye_password_show);
            eyeHide = ContextCompat.getDrawable(context, R.drawable.eye_password_hide);
        }
        setCompoundDrawable(editText, eyeHide);
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isTouchWithinDrawableBounds(editText, event)) {
                    togglePasswordVisibility(editText);
                    return true;
                }
            }
            return false;
        });
    }

    // Generate mark sheet PDF file and save it to the device download folder
    public static boolean checkStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity,
                new String[]{ android.Manifest.permission.WRITE_EXTERNAL_STORAGE },
                PERMISSION_REQUEST_CODE
        );
    }
    public static void saveMarkSheetAsPDF(Context context, String semester, StudentInfo studentInfo, ArrayList<MarkSheetData> markSheet) {
        File downloadsDir, file;
        try {
            downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(downloadsDir, "" + studentInfo.studentId + "_" + semester + "_mark sheet.pdf");
        } catch (Exception e) {
            Toast.generalError(context, "Unable to save mark sheet");
            return;
        }

        try {
            StudentDBHandler studentDBHandler = StudentDBHandler.getInstance(context);
            Document document = new Document(PageSize.A4);
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
            pdfWriter.setPageEvent(new FooterEvent());
            Font headingFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD);
            Font mainFont = new Font(Font.HELVETICA, 14, Font.NORMAL);
            Font subHeadingFont = new Font(Font.TIMES_ROMAN, 16, Font.BOLD);
            document.open();

            // Add student info
            document.add(new Paragraph("Student ID   : " + studentInfo.studentId, mainFont));
            document.add(new Paragraph("Name          : " + studentInfo.name, mainFont));
            document.add(new Paragraph("Session       : " + studentDBHandler.getSession(studentInfo.sessionId).desc, mainFont));
            document.add(new Paragraph("Department : " + studentDBHandler.getDepartment(studentInfo.deptId).longDesc, mainFont));
            document.add(new Paragraph("\n\n", mainFont));
            Paragraph title = new Paragraph(semester + " Mark Sheet", headingFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n", mainFont));

            // Add mark sheet table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);
            table.setWidths(new float[]{1, 4, 1, 1, 1});
            // table.setHorizontalAlignment(Element.ALIGN_CENTER);
            // table.getDefaultCell().setPadding(5);

            PdfPCell codeCell, courseCell, markCell, gpaCell, gradeCell;
            codeCell = new PdfPCell(new Phrase("Code", subHeadingFont));
            courseCell = new PdfPCell(new Phrase("Course", subHeadingFont));
            markCell = new PdfPCell(new Phrase("Mark", subHeadingFont));
            gpaCell = new PdfPCell(new Phrase("GPA", subHeadingFont));
            gradeCell = new PdfPCell(new Phrase("Grade", subHeadingFont));

            codeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            codeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            codeCell.setPadding(5f);
            courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            courseCell.setPadding(8f);
            markCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            markCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            markCell.setPadding(5f);
            gpaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            gpaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            gpaCell.setPadding(5f);
            gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            gradeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            gradeCell.setPadding(5f);

            table.addCell(codeCell);
            table.addCell(courseCell);
            table.addCell(markCell);
            table.addCell(gpaCell);
            table.addCell(gradeCell);

            for (MarkSheetData markSheetData : markSheet) {
                codeCell = new PdfPCell(new Phrase(markSheetData.courseCode, mainFont));
                courseCell = new PdfPCell(new Phrase(markSheetData.courseDesc, mainFont));
                markCell = new PdfPCell(new Phrase(markSheetData.mark, mainFont));
                gpaCell = new PdfPCell(new Phrase(markSheetData.gpa, mainFont));
                gradeCell = new PdfPCell(new Phrase(markSheetData.grade, mainFont));

                codeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                codeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                codeCell.setPadding(5f);
                courseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                courseCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                courseCell.setPadding(8f);
                markCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                markCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                markCell.setPadding(5f);
                gpaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                gpaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                gpaCell.setPadding(5f);
                gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                gradeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                gradeCell.setPadding(5f);

                table.addCell(codeCell);
                table.addCell(courseCell);
                table.addCell(markCell);
                table.addCell(gpaCell);
                table.addCell(gradeCell);
            }
            /*
            table.addCell("Course Code");
            table.addCell("Course Description");
            table.addCell("Mark");
            table.addCell("GPA");
            table.addCell("Grade");
            for (MarkSheetData markSheetData : markSheet) {
                table.addCell(markSheetData.courseCode);
                table.addCell(markSheetData.courseDesc);
                table.addCell(markSheetData.mark);
                table.addCell(markSheetData.gpa);
                table.addCell(markSheetData.grade);
            }
            */
            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.generalError(context, "Unable to save mark sheet");
        }
    }
}