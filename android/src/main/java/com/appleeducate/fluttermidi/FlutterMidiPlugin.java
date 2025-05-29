package com.appleeducate.fluttermidi;

import android.content.Context;
import cn.sherlock.com.sun.media.sound.SF2Soundbank;
import cn.sherlock.com.sun.media.sound.SoftSynthesizer;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.BinaryMessenger;
import java.io.File;
import java.io.IOException;
import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MidiUnavailableException;
import jp.kshoji.javax.sound.midi.Receiver;
import jp.kshoji.javax.sound.midi.ShortMessage;

/** FlutterMidiPlugin */
public class FlutterMidiPlugin implements MethodCallHandler, FlutterPlugin {
  private SoftSynthesizer synth;
  private Receiver recv;
  private MethodChannel methodChannel;
  private Context applicationContext;

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    this.applicationContext = binding.getApplicationContext();
    methodChannel = new MethodChannel(binding.getBinaryMessenger(), "flutter_midi");
    methodChannel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    applicationContext = null;
    methodChannel.setMethodCallHandler(null);
    methodChannel = null;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    try {
      if (call.method.equals("prepare_midi")) {
        String _path = call.argument("path");
        File _file = new File(_path);
        SF2Soundbank sf = new SF2Soundbank(_file);
        synth = new SoftSynthesizer();
        synth.open();
        synth.loadAllInstruments(sf);
        synth.getChannels()[0].programChange(0);
        synth.getChannels()[1].programChange(1);
        recv = synth.getReceiver();
        result.success(null);
      } else if (call.method.equals("change_sound")) {
        String _path = call.argument("path");
        File _file = new File(_path);
        SF2Soundbank sf = new SF2Soundbank(_file);
        synth = new SoftSynthesizer();
        synth.open();
        synth.loadAllInstruments(sf);
        synth.getChannels()[0].programChange(0);
        synth.getChannels()[1].programChange(1);
        recv = synth.getReceiver();
        result.success(null);
      } else if (call.method.equals("play_midi_note")) {
        int _note = call.argument("note");
        int _velocity = call.argument("velocity");
        ShortMessage msg = new ShortMessage();
        msg.setMessage(ShortMessage.NOTE_ON, 0, _note, _velocity);
        recv.send(msg, -1);
        result.success(null);
      } else if (call.method.equals("stop_midi_note")) {
        int _note = call.argument("note");
        int _velocity = call.argument("velocity");
        ShortMessage msg = new ShortMessage();
        msg.setMessage(ShortMessage.NOTE_OFF, 0, _note, _velocity);
        recv.send(msg, -1);
        result.success(null);
      } else {
        result.notImplemented();
      }
    } catch (IOException e) {
      result.error("IO_ERROR", "Failed to load soundbank: " + e.getMessage(), null);
    } catch (MidiUnavailableException e) {
      result.error("MIDI_UNAVAILABLE", "MIDI synthesizer unavailable: " + e.getMessage(), null);
    } catch (InvalidMidiDataException e) {
      result.error("INVALID_MIDI_DATA", "Invalid MIDI data: " + e.getMessage(), null);
    } catch (Exception e) {
      result.error("UNKNOWN_ERROR", "An unexpected error occurred: " + e.getMessage(), null);
    }
  }
}
