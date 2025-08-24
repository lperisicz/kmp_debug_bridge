# Debug Bridge

A desktop application for Android QA testing with integrated debugging and recording capabilities.

### Key Features
- **Device Management**: Connect via USB (ADB) or wireless pairing with QR code scanning
- **Multi-format Recording**: Simultaneous capture of:
  - Screen recordings (MP4 via scrcpy)
  - Application logs (logcat with process filtering)
  - Device properties (getprop output)
- **App Installation**: Install and launch APKs on connected devices
- **Session Management**: Organized recording sessions with timestamped outputs

### Screens
1. **Recording**: Start/stop comprehensive debug sessions
2. **Install**: Deploy APK files to devices
3. **Settings**: Configure workspace, package names, and device selection

### Requirements
- **ADB**: Android Debug Bridge must be installed
- **scrcpy**: Required for screen recording functionality
- **Desktop OS**: Currently supports macOS (.dmg), Windows (.msi), and Linux (.deb)

### Output Structure
Recording sessions create organized directories:
```
workspace/
  └── timestamp/
      ├── screen_recording.mp4
      ├── logs-1.txt
      ├── logs-2.txt (if app restarts)
      └── props.txt
      timestamp.zip
```

**Note**: iOS support is planned for future releases.