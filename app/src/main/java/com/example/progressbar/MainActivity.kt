package com.example.progressbar

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var progressIndicator: ProgressBar
    lateinit var texttoshow: TextView

    var timeleft=0;
    var count = 60 - timeleft;
    var play = false;
    var stop = false;
    var startplaying = false;
    var start=0;
    var timmer = 0;
    lateinit var mr : MediaRecorder

    lateinit var countDownTimer: CountDownTimer
    var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    .toString() + StringBuilder("/").append(
        SimpleDateFormat("MM-yyyy-hh_mm_ss")
            .format(Date())
    ).append(".mp4").toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressIndicator = findViewById(R.id.progressindicator)
        texttoshow = findViewById(R.id.textview_countdown)

        val startRecording : Button = findViewById(R.id.button1)
        val stopRecording : Button = findViewById(R.id.button2)
        val playRecording : Button = findViewById(R.id.button3)


   mr = MediaRecorder()




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )

        }

        startRecording.setOnClickListener {

            if(play == true){
                Toast.makeText(this, "Already in progress", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    startTimer(60000)
                    mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mr.setOutputFile(path);try {
                        mr.prepare()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    mr.start()
                } catch (e:IOException,  ){
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }

            }


        }

        stopRecording.setOnClickListener { if(stop == false){
            Toast.makeText(this, "Cannot stop", Toast.LENGTH_SHORT).show()
        }else{

            try {
                if(timeleft>=55){
                    Toast.makeText(this, "Has to be of more then 5 sec", Toast.LENGTH_SHORT).show()
                }else{  mr.stop()
                    countDownTimer.cancel()
                     start = timeleft

                    startplaying = true;
                    Toast.makeText(this, "Recording Saved", Toast.LENGTH_SHORT).show()
                    texttoshow.text="Play"}
                progressIndicator.setProgress(0)
            } catch (ex: RuntimeException) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
            }

        }

                }
        playRecording.setOnClickListener{
       if(startplaying == false){
           Toast.makeText(this, "Nothing to play", Toast.LENGTH_SHORT).show()
       }else{
           var mp = MediaPlayer()
           mp.setDataSource(path)
           mp.prepare()
           mp.start()
           stop= true
           progressIndicator.setProgress(0);
           progressIndicator.progress = 0;

           playBack((60-timeleft)*1000)


       }

        }
    }



    private  fun startTimer(intcountdowntime: Int){
        stop = true
        progressIndicator.max=100


        val lngControllertime = intcountdowntime.toLong();
           play = true

        countDownTimer = object : CountDownTimer(lngControllertime, 1000) {
            var progress = 0;


            override fun onTick(p0: Long) {

                var totaltime = lngControllertime/1000
                timeleft = (p0/1000).toInt()
                progress = (timeleft*100/totaltime).toInt()

                count = 60-timeleft;
                progressIndicator.progress=progress


                texttoshow.text= "0:"+timeleft.toString()
            }

            override fun onFinish() {
                texttoshow.text="0"
                stop = false;
                play = false
                startplaying = false
            }
        }.start()


    }



    private  fun playBack(intcountdowntime: Int){
        stop = true



        val lngControllertime = intcountdowntime.toLong();
        play = true

        countDownTimer = object : CountDownTimer(lngControllertime, 10) {
            var progress = 0;


            override fun onTick(p0: Long) {

                var totaltime = lngControllertime/10
                timeleft = (p0/10).toInt()
                progress = (timeleft*100/totaltime).toInt()

                count = 60-timeleft;
                progressIndicator.progress=progress


                texttoshow.text= "0:"+timeleft.toString()
            }

            override fun onFinish() {
                texttoshow.text="0"
                stop = false;
                play = false
                startplaying = false
            }
        }.start()


    }


    private  fun trytmier(intcountdowntime: Int){
        stop = true

 progressIndicator.max=60
        val lngControllertime = intcountdowntime.toLong();
        play = true

        countDownTimer = object : CountDownTimer(lngControllertime, 1000) {
            var progress = 0;


            override fun onTick(p0: Long) {

               progress= progress+1
                progressIndicator.progress=progress
                timeleft = (p0/1000).toInt()

                texttoshow.text= "0:"+timeleft.toString()
            }

            override fun onFinish() {
                texttoshow.text="0"
                stop = false;
                play = false
                progressIndicator.setProgress(0)
                startplaying = false
            }
        }.start()


    }
}