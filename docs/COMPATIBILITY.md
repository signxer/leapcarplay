# Compatibility

This public preview is an independent receiver, not an Apple-certified CarPlay accessory. The experimental bundled accessory identity is extractable and its future acceptance is not guaranteed.

| Area | Current scope |
| --- | --- |
| Head unit | Android 9+ APK; wireless Wi-Fi Direct path needs Android 10+; Leapmotor 8155 is a target platform, not yet independently verified by this project |
| Phone | Standard, non-jailbroken iPhone with CarPlay enabled; device/iOS compatibility varies |
| Physical evidence | Previous private builds: wired and wireless picture, touch and audio confirmed on a BYD development car with iPhone XS / iOS 18.7.10; this does not verify Leapmotor 8155 |
| Other cars | Mixed community reports across DiLink generations; not a certified model support list |
| Inherited BYD-specific functions | HUD/street names and car-hotspot behavior were verified on a BYD development car only; these functions are separate from HDMI2 CarPlay video output |
| Wi-Fi | Prefer 5 GHz without an established station connection; align to a supported existing station channel; explicit 2.4 GHz fallback for firmware that rejects 5 GHz or automatic channel selection |
| Video | Default H.264 / 60 fps; 30 fps remains available if the head unit drops frames or overheats; HEVC is optional |

## CarPlay secondary display

When Android exposes a presentation display named HDMI2, LeapCarPlay advertises it as CarPlay's optional cluster display and routes the alternate-screen stream to that display. If there is no HDMI2-named display, LeapCarPlay uses the only available presentation display; when several unnamed presentation displays exist, it leaves the cluster output disabled. HDMI2 is output-only and does not receive touch events. The iPhone controls the content of the alternate stream, so a connected display alone does not guarantee that every navigation app will send a map view to it. The main CarPlay screen remains on the head unit display.

## BYD HUD and car hotspot

See [BYD navigation](BYD_NAVIGATION.md) for exact verified firmware and lifecycle limits of that BYD-specific feature. The results do not establish support on Leapmotor firmware.

## Known limitations

- Some units stutter, particularly under higher video load. A 2.4 GHz link alone does not prove the cause: interference, firmware and decoder stalls can all contribute. If this happens, switch to 30 fps, lower the resolution and attach a report.
- Some iOS/head-unit combinations do not visibly apply icon and text size. Reconnection is implemented; that does not guarantee the iPhone chooses the requested layout.
- A radio that supports joining a 5 GHz network may still reject a 5 GHz Wi-Fi Direct group. The capability flag is diagnostic, not proof of group-owner support.
- Automatic startup depends on the car's firmware and startup permissions.
- 60 fps is the new-install default, but simultaneous main-screen and cluster decoding, sustained frame rate and thermal behavior have not been measured on an 8155 vehicle. Select 30 fps if the unit stutters or runs hot.
- USB requires a data port and correct host/device-role behavior.
- Calls, Siri, background reconnection, long journeys and future iOS releases need broader testing.

Reports record requested and actual frequencies, station association state, fallback failures and remembered-configuration events. Wi-Fi credentials and protocol payloads are excluded. A successful hotspot is not itself a successful CarPlay session.

Android references: [SupplicantState](https://developer.android.com/reference/android/net/wifi/SupplicantState), [explicit P2P operating frequency](https://developer.android.com/reference/android/net/wifi/p2p/WifiP2pConfig.Builder#setGroupOperatingFrequency(int)).
