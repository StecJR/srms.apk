-keep class javax.mail.** { *; }
-keep class javax.mail.auth.** { *; }
-keep class javax.mail.internet.** { *; }
-keep class javax.mail.Transport { *; }

-keep class com.sun.mail.smtp.** { *; }
-keep class com.sun.mail.util.** { *; }
-keep class com.sun.mail.handlers.** { *; }
-keep class com.sun.mail.smtp.SMTPTransport { *; }

-dontwarn org.apache.fop.complexscripts.fonts.GlyphSubstitutionTable
-dontwarn org.apache.fop.complexscripts.util.CharScript
-dontwarn org.apache.fop.complexscripts.util.GlyphSequence
-dontwarn org.apache.fop.fonts.apps.TTFReader
-dontwarn org.apache.fop.fonts.truetype.FontFileReader
-dontwarn org.apache.fop.fonts.truetype.TTFFile

