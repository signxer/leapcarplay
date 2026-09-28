# Install and connect

No LeapCarPlay APK release is published yet. Build the app using [the build instructions](BUILD.md), then install the generated APK on the Android head unit. Do not install it on the iPhone.

1. Park the car and install the APK using the method supported by the head unit.
2. Open LeapCarPlay. Grant the permissions requested for the features you use: Bluetooth/Nearby devices, Wi-Fi/Location on older Android, and microphone for Siri/calls. Allow notifications for connection controls.
3. Close other phone-projection apps before connecting.

## Wireless

Android 10+ and functioning Wi-Fi Direct are required. Pair your iPhone through the car's Bluetooth settings, keep Bluetooth and Wi-Fi enabled, then choose **Connect phone** in LeapCarPlay. Select your paired iPhone and allow CarPlay on the phone if prompted. **Choose iPhone** changes the selected paired device. No manual MAC address entry or ADB setup is needed.

Your car's normal internet connection can remain enabled. LeapCarPlay aligns channels only to a completed Wi-Fi association; otherwise it prefers 5 GHz, with fixed 2.4 GHz channels as fallback. Firmware makes the final radio decision. Do not change the car's country code or driver properties.

## USB

Connect the iPhone to a USB **data** port with a data-capable cable and choose **Connect with USB**. Approve USB access, Trust/CarPlay and the local VPN permission if requested. The local VPN carries the USB network link; it is not an internet VPN service. Charge-only ports/cables cannot work.

## Settings

Swipe down with three fingers in CarPlay to open LeapCarPlay settings, or return to the home screen. Icon/text size, resolution and frame rate use **Apply and reconnect** during an active session. A selection alone does not apply; Cancel preserves the old setting. When disconnected, **Save** applies to the next connection. Other settings also apply on the next connection.

Start with 30 fps, Efficient video (HEVC) off and Default icon/text size. Try 80% or 60% resolution for a slower head unit. Some iPhone/head-unit combinations still ignore icon/text scaling.

## Connection recovery and reports

If reinstalling left an old group, close other projection apps, then use **Settings → Wireless connection help → Reset CarPlay Wi-Fi**. LeapCarPlay asks before removing an unrecognized Wi-Fi Direct group. Updating in place is preferable to uninstalling.

Use **Settings → Save diagnostic report** after reproducing a problem. Android 10+ saves to **Downloads/LeapCarPlay**; Android 9 uses a document picker. Review the file, then attach it to a GitHub issue with car model, DiLink/Android versions, iPhone/iOS, transport and reproduction steps. Nothing is uploaded automatically.

APK installation restrictions are controlled by your car's firmware. ADB is optional if your car supports it, not an app runtime requirement:

```sh
adb install -r mobile/build/outputs/apk/debug/mobile-debug.apk
```

Only use a trusted computer. A different signing certificate cannot update this build; do not uninstall until you have saved any reports you need.

## BYD navigation

See [BYD navigation displays](BYD_NAVIGATION.md) for the firmware scope, map metadata requirements, settings and cleanup behavior. No runtime ADB starter is required.
