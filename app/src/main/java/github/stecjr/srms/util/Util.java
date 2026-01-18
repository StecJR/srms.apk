package github.stecjr.srms.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.DatePicker;
import android.widget.EditText;

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
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import github.stecjr.srms.R;
import github.stecjr.srms.database.StudentDBHandler;
import github.stecjr.srms.model.MarkSheetData;
import github.stecjr.srms.model.StudentInfo;

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
    private static final DateTimeFormatter dateTimeInputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateTimeOutputFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final Pattern validPhonePattern = Pattern.compile("^(01\\d{9}|\\+8801\\d{9})$");
    private static final Pattern validEmailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final SecureRandom random = new SecureRandom();
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
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static LocalDateTime getFormatedDate(String date) {
        try {
            return LocalDateTime.parse(date, dateTimeInputFormat);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String getFormatedDate(Date date) {
        try {
            return dateTimeOutputFormat.format(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static String getFormatedStringDate(String date) {
        try {
            return LocalDateTime.parse(date, dateTimeInputFormat).format(dateTimeOutputFormat);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static int getOTP() {
        return 100000 + random.nextInt(900000);
    }

    // Password show/hide
    private static void setCompoundDrawable(EditText editText, Drawable drawable) {
        editText.setCompoundDrawablesWithIntrinsicBounds(editText.getCompoundDrawables()[0], editText.getCompoundDrawables()[1], drawable, editText.getCompoundDrawables()[3]);
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
    public static void togglePasswordVisibility(Context context, EditText editText) {
        if (eyeShow == null || eyeHide == null) {
            eyeShow = ContextCompat.getDrawable(context, R.drawable.eye_show);
            eyeHide = ContextCompat.getDrawable(context, R.drawable.eye_hide);
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

    // Generate and save mark sheet PDF
    public static void saveMarkSheetAsPDF(Context context, String semester, StudentInfo studentInfo, ArrayList<MarkSheetData> markSheet) {
        File downloadsDir, file;
        try {
            downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(downloadsDir, studentInfo.studentId + "_" + semester + "_mark sheet.pdf");
        } catch (Exception e) {
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
            document.add(table);
            document.close();
        } catch (Exception e) {
            Toast.generalError(context, "Unable to save mark sheet");
        }
    }

    // Date picker
    private static void updateUI(Calendar calendar, EditText view) {
        view.setText(getFormatedDate(calendar.getTime()));
    }

    public static void pickDate(Context context, Calendar calendar, EditText view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (DatePicker pickerView, int year, int month, int dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateUI(calendar, view);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    // Simple validations
    public static boolean validPhoneNumber(String number) {
        return validPhonePattern.matcher(number).matches();
    }

    public static boolean validEmail(String email) {
        return validEmailPattern.matcher(email).matches();
    }

    // Image handler
    public static void openImagePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, PermissionHandler.REQUEST_PICK_IMAGE);
    }

    public static boolean imageExistsOnInternalStorage(Context context, String imageName) {
        File file = new File(context.getFilesDir(), imageName);
        return file.exists();
    }

    public static void saveImageToInternalStorage(Context context, Uri imageUri, String imageName) {
        if (imageUri == null) {
            Toast.generalError(context, "No image selected");
            return;
        }
        try {
            Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), imageUri));

            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int newWidth = 256, newHeight = 256;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            float scale = Math.min(scaleWidth, scaleHeight);

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale, (float) width / 2, (float) height / 2);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

            File file = new File(context.getFilesDir(), imageName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Toast.generalError(context, "Failed to save image");
        }
    }

    public static Bitmap getImageFromInternalStorage(Context context, String imageName) {
        File file = new File(context.getFilesDir(), imageName);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static int renameImageFromInternalStorage(Context context, String oldName, String newName) {
        File oldFile = new File(context.getFilesDir(), oldName);
        File newFile = new File(context.getFilesDir(), newName);

        if (!oldFile.exists()) return -1;
        if (newFile.exists()) return 1;

        if (oldFile.renameTo(newFile)) return 0;
        else return -2;
    }

    public static boolean deleteImageFromInternalStorage(Context context, String imageName) {
        File file = new File(context.getFilesDir(), imageName);
        if (file.exists()) return file.delete();
        else return false;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (network != null)
                    return Objects.requireNonNull(connectivityManager.getNetworkCapabilities(network)).hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            } else {
                if (network != null) {
                    NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                    if (capabilities != null)
                        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                }
            }
        }
        return false;
    }
}