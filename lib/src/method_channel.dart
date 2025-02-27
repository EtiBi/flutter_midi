import 'package:flutter/services.dart';

import 'platform_interface.dart';

const MethodChannel _channel = MethodChannel('flutter_midi');

class MethodChannelUrlLauncher extends FlutterMidiPlatform {
  /// Needed so that the sound font is loaded
  /// On iOS make sure to include the sound_font.SF2 in the Runner folder.
  /// This does not work in the simulator.
  @override
  static Future<String?> prepare({
    required ByteData? sf2,
    String name = 'instrument.sf2',
  }) async {
    return _channel.invokeMethod<String>('prepare_midi', {
      'name': name,
      'data': sf2,
    });
  }

  /// Needed so that the sound font is loaded
  /// On iOS make sure to include the sound_font.SF2 in the Runner folder.
  /// This does not work in the simulator.
  @override
  static Future<String?> changeSound({
    required ByteData? sf2,
    String name = 'instrument.sf2',
  }) async {
    return _channel.invokeMethod<String>('change_sound', {
      'name': name,
      'data': sf2,
    });
  }

  /// Unmute the device temporarily even if the mute switch is on or toggled in settings.
  @override
  static Future<String?> unmute() {
    return _channel.invokeMethod<String>('unmute');
  }

  /// Use this when stopping the sound onTouchUp or to cancel a long file.
  /// Not needed if playing midi onTap.
  /// Stop with velocity in the range between 0-127
  @override
  static Future<String?> stopMidiNote({
    required int midi,
    int velocity = 64,
  }) {
    return _channel.invokeMethod<String>('stop_midi_note', {
      'note': midi,
      'velocity': velocity,
    });
  }

  /// Play a midi note from the sound_font.SF2 library bundled with the application.
  /// Play a midi note in the range between 0-127
  /// Play with velocity in the range between 0-127
  /// Multiple notes can be played at once as separate calls.
  @override
  static Future<String?> playMidiNote({
    required int midi,
    int velocity = 64,
  }) {
    return _channel.invokeMethod<String>('play_midi_note', {
      'note': midi,
      'velocity': velocity,
    });
  }
}
