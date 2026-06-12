# Pdfium викликає Java-класи з нативного коду за іменами —
# мінімізатор не повинен їх перейменовувати чи видаляти
-keep class com.shockwave.** { *; }
-keep class com.github.barteksc.pdfviewer.** { *; }
