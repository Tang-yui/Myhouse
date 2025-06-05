## TeacherConsultApp

This repository contains the source code for TeacherConsultApp, an Android application prototype.

The app requests runtime permissions for contacts, phone state, and audio recording. A foreground service monitors call state and records calls to the app's private storage.
When a call ends the recording path is passed to a summary screen where the audio can be transcribed and a consultation log saved locally.

Recent call records are stored as JSON in the app's private storage. They appear
on the main screen and can be tapped to view the saved summary and play the
recorded audio.
