androged
========

Android port of Generalized Edit Distance web application

Project is created at the course "Text Algorithms" (J.Vilo) at University of Tartu.
The goal of the project was to port existing Generalized Edit Distance web application to Android platform, to estimate principal possibility of intensive generalized edit distance algorithm on modern mobile platform.
Application is built for Android API 9 (Gingerbread, 2.3+), and uses native code for distance calculation. <b>It should be compiled NOT USING original Google Android NDK, but <a href='http://cryxtax.net'>project CryxtaX modified NDK r7 by D.Moskalchuk </a> due to missing wide and multibyte function support in original NDK (at least for r8)</b>.
Project does not implement all functions of <a href='https://biit-dev.cs.ut.ee/~orasmaa/gen_ed_test/'>original application</a>, but successfully proves possibility of applying this algorithm "offline" on mobile platform even for middle-end devices (tested on 1-core 800MHz Motorola DEFY, Android 2.3), and shows perspective for further development and goot potential for improvements. Current version has multiple memory issues, therefore usually has to be restarted before making next search.
As additional note, next improvement (after fixing memory issues) could be a) managing user-defined transformation rules b) parallelising parts of Generalized Edit Distance algorithm for using existing and upcoming multi-core processor support.
For any questions about this project please contact me at Github/glebsts.

05.01.2013