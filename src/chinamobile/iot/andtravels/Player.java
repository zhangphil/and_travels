package chinamobile.iot.andtravels;

import java.io.FileDescriptor;
import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class Player implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {

	private String TAG = "Player";
	private String mUrl;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private static Player player;

	public static Player getInstance() {
		if (player == null) {
			player = new Player();
		} else {

		}

		return player;
	}

	public Player() {
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	public void play() {
		mediaPlayer.start();
	}

	public void preparePlay(FileDescriptor url) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url); // 设置数据源
			mediaPlayer.prepare(); // prepare自动播放
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	// 停止
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(android.media.MediaPlayer mp) {
		// TODO Auto-generated method stub
		mediaPlayer.start();
	}

	@Override
	public void onBufferingUpdate(android.media.MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(android.media.MediaPlayer mp) {
		// TODO Auto-generated method stub
		// 播放完成需要处理一下
	}

}
