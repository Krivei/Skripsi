package com.example.personalrecordv4

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.personalrecordv4.viewmodel.HistoryViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import java.util.concurrent.ExecutionException


class ExerciseActivity : AppCompatActivity() {
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var PERMISSION_REQUESTS = 1
    private var previewView: PreviewView? = null

    private var options = PoseDetectorOptions.Builder()
        .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
        .build()
    private var poseDetector = PoseDetection.getClient(options)
    private var canvas: Canvas? = null
    private var mPaint = Paint()
    private var display: Display? = null
    private var bitmap4Save: Bitmap? = null
    private var bitmapArrayList = ArrayList<Bitmap?>()
    private var bitmap4DisplayArrayList = ArrayList<Bitmap?>()
    private var poseArrayList = ArrayList<Pose>()
    private var isRunning = false
    private var exerciseCounter = 0
    private var sets = 0
    private var name = ""
    private var rep = 0
    private var exerciseState = "stretch"
    private var currentSet = 1
    private var status = ""
    private var finisher = ""
    private var historyId = ""
    private var weight = 0
    private var keeper = false
    private var timer: CountDownTimer? = null
    private var weightList = mutableListOf<Int>()
    private var repList = mutableListOf<Int>()
    private lateinit var counter : AppCompatTextView
    private lateinit var timertext : AppCompatTextView
    private lateinit var title : AppCompatTextView
    private lateinit var notice : AppCompatTextView
    private lateinit var tip : AppCompatTextView
    private lateinit var bodyparts : AppCompatTextView
    private lateinit var setDisplay : AppCompatTextView
    private lateinit var mediaPlayer: MediaPlayer

    private val historyViewModel : HistoryViewModel by viewModels()
    @ExperimentalGetImage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        sets = intent.getIntExtra("Set",1)
        name = intent.getStringExtra("Name").toString()
        rep = intent.getIntExtra("Rep",5)
        status = intent.getStringExtra("Status").toString()
        historyId = intent.getStringExtra("historyId").toString()
        Log.i("ExerciseNih","History ID : $historyId")
        weight = intent.getIntExtra("weight",0)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        var cancel = findViewById<ImageView>(R.id.cancelExercise)
        var giveup = findViewById<ImageView>(R.id.ivGiveUp)
        var setLayout = findViewById<ConstraintLayout>(R.id.clSet)
        setDisplay = findViewById(R.id.tvCurrentExercise)
        if (status=="Tutorial"){
            giveup.visibility = View.GONE
            setLayout.visibility = View.GONE
        } else {
            cancel.visibility = View.GONE
        }
        cancel.setOnClickListener {
            finish()
        }
        giveup.setOnClickListener {
            if (exerciseCounter>=rep){
                nextSetDialog()
            } else {
                unbindCamera()
                giveUpDialog()
            }
        }
        previewView = findViewById(R.id.previewView)
        display = findViewById(R.id.displayOverlay)
        counter = findViewById(R.id.tvRepetition)
        timertext = findViewById(R.id.tvTimer)
        title = findViewById(R.id.tvActivityTitle)
        title.text = name
        notice = findViewById(R.id.notice)
        tip = findViewById(R.id.tip)
        bodyparts = findViewById(R.id.tvBodyParts)
        mPaint.color = Color.GREEN
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.strokeWidth = 10f
        timer = object : CountDownTimer(5000,1000){
            override fun onTick(millisUntilFinished: Long) {
                unbindCamera()
                giveup.visibility = View.GONE
                finisher=""
                notice.visibility = View.VISIBLE
                timertext.text = (millisUntilFinished/1000).toString()
                tip.visibility = View.VISIBLE
                bodyparts.visibility = View.VISIBLE
                timertext.visibility = View.VISIBLE
                if (name.contains("Press")||name.contains("Pull Up")||name.contains("Push Up")){
                    bodyparts.text = "Elbows and Shoulders"
                } else if (name.contains("Bicep")||name.contains("Skull")) {
                    bodyparts.text = "Elbows and Wrists"
                } else if(name.contains("Deadlift")) {
                    bodyparts.text = "Knees and Wrists"
                } else if(name.contains("Squat")) {
                    bodyparts.text = "Knees and Hips"
                } else if(name.contains("Row")){
                    bodyparts.text = "Elbows and Hips"
                }
            }
            override fun onFinish() {
                if (status=="Exercise"){
                    keeper = !keeper
                    giveup.visibility = View.VISIBLE
                    setDisplay.text = "$currentSet/$sets"
                }
                notice.visibility = View.GONE
                tip.visibility = View.GONE
                bodyparts.visibility = View.GONE
                timertext.visibility = View.GONE
                cameraProviderFuture!!.addListener({
                    try {
                        val cameraProvider = cameraProviderFuture!!.get()
                        bindPreview(cameraProvider)
                    } catch (e: ExecutionException) {
                    } catch (e: InterruptedException) {
                    }
                }, ContextCompat.getMainExecutor(this@ExerciseActivity))
                if (!allPermissionsGranted()) {
                    runtimePermissions
                }
            }
        }
        timer!!.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindCamera()
        timer?.cancel()
        mediaPlayer.release()
    }

    var RunMlkit = Runnable {
        poseDetector.process(InputImage.fromBitmap(bitmapArrayList[0]!!, 0))
            .addOnSuccessListener { pose ->
                poseArrayList.add(
                    pose
                )
                if (isExerciseCycleDetected(pose,name)) {
                    exerciseCounter++
                    if (status=="Tutorial"){
                        counter.text = "${exerciseCounter}"
                    } else {
                        counter.text = "${exerciseCounter} / ${rep}"
                    }
                }
                updateExerciseState(pose,name)
            }.addOnFailureListener { }
    }

    @ExperimentalGetImage
    fun bindPreview(cameraProvider: ProcessCameraProvider) {
        Log.i("BindCalled","Bind")
        val preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        preview.setSurfaceProvider(previewView!!.surfaceProvider)
        val imageAnalysis =
            ImageAnalysis.Builder()
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
        imageAnalysis.setAnalyzer(
            ActivityCompat.getMainExecutor(this)

        ) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val byteBuffer = imageProxy.image!!.planes[0].buffer
            byteBuffer.rewind()
            val bitmap = Bitmap.createBitmap(
                imageProxy.width,
                imageProxy.height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.copyPixelsFromBuffer(byteBuffer)
            val matrix = Matrix()
            matrix.postRotate(270f)
            matrix.postScale(-1f, 1f)
            val rotatedBitmap = Bitmap.createBitmap(
                bitmap,
                0,
                0,
                imageProxy.width,
                imageProxy.height,
                matrix,
                false
            )
            bitmapArrayList.add(rotatedBitmap)
            if (poseArrayList.size >= 1) {
                canvas = Canvas(bitmapArrayList[0]!!)
                for (poseLandmark in poseArrayList[0].allPoseLandmarks) {

                    if(name.contains("Press")||name.contains("Pull Up")||name.contains("Push Up")){
                        when(poseLandmark.landmarkType){
                            PoseLandmark.LEFT_ELBOW,PoseLandmark.RIGHT_ELBOW,PoseLandmark.RIGHT_SHOULDER,PoseLandmark.LEFT_SHOULDER->{
                                canvas!!.drawCircle(
                                    poseLandmark.position.x,
                                    poseLandmark.position.y,
                                    5f,
                                    mPaint
                                )
                            }
                        }
                    } else if(name.contains("Bicep")||name.contains("Skull")){
                        when(poseLandmark.landmarkType){
                            PoseLandmark.LEFT_ELBOW,PoseLandmark.RIGHT_ELBOW,PoseLandmark.LEFT_WRIST,PoseLandmark.RIGHT_WRIST -> {
                                canvas!!.drawCircle(
                                    poseLandmark.position.x,
                                    poseLandmark.position.y,
                                    5f,
                                    mPaint
                                )
                            }
                        }
                    } else if(name.contains("Deadlift")){
                        when(poseLandmark.landmarkType){
                            PoseLandmark.LEFT_KNEE,PoseLandmark.RIGHT_KNEE,PoseLandmark.LEFT_WRIST,PoseLandmark.RIGHT_WRIST -> {
                                canvas!!.drawCircle(
                                    poseLandmark.position.x,
                                    poseLandmark.position.y,
                                    5f,
                                    mPaint
                                )
                            }
                        }
                    } else if(name.contains("Squat")){
                        when(poseLandmark.landmarkType){
                            PoseLandmark.LEFT_KNEE,PoseLandmark.RIGHT_KNEE,PoseLandmark.LEFT_HIP,PoseLandmark.RIGHT_HIP -> {
                                canvas!!.drawCircle(
                                    poseLandmark.position.x,
                                    poseLandmark.position.y,
                                    5f,
                                    mPaint
                                )
                            }
                        }
                    } else if(name.contains("Row")){
                        when(poseLandmark.landmarkType){
                            PoseLandmark.LEFT_ELBOW,PoseLandmark.RIGHT_ELBOW,PoseLandmark.LEFT_HIP,PoseLandmark.RIGHT_HIP -> {
                                canvas!!.drawCircle(
                                    poseLandmark.position.x,
                                    poseLandmark.position.y,
                                    5f,
                                    mPaint
                                )
                            }
                        }
                    }
                }
                bitmap4DisplayArrayList.clear()
                bitmap4DisplayArrayList.add(bitmapArrayList[0])
                bitmap4Save = bitmapArrayList[bitmapArrayList.size - 1]
                bitmapArrayList.clear()
                bitmapArrayList.add(bitmap4Save)
                poseArrayList.clear()
                isRunning = false
            }
            if (poseArrayList.size == 0 && bitmapArrayList.size >= 1 && !isRunning) {
                RunMlkit.run()
                isRunning = true
            }
            if (bitmap4DisplayArrayList.size >= 1) {
                display!!.getBitmap(bitmap4DisplayArrayList[0])
            }
            imageProxy.close()
        }
        val camera = cameraProvider.bindToLifecycle(
            (this as LifecycleOwner),
            cameraSelector,
            imageAnalysis,
            preview
        )

    }
    private fun isExerciseCycleDetected(pose: Pose, name: String): Boolean {

        val shoulderLeft = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val shoulderRight = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val elbowLeft = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val elbowRight = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val wristLeft = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val wristRight = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val hipLeft = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val hipRight = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val kneeLeft = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val kneeRight = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        if (status=="Exercise"){
            if (exerciseCounter==rep){
                keeper = !keeper
                if (keeper==true){
                    if (!this::mediaPlayer.isInitialized){
                        mediaPlayer = MediaPlayer.create(this, R.raw.notif)
                    }
                    if (mediaPlayer.isPlaying){
                        mediaPlayer.pause()
                        mediaPlayer.seekTo(0)
                    }
                    mediaPlayer.start()
                    unbindCamera()
                    repList.add(exerciseCounter)
                    weightList.add(weight)
                    if (currentSet==sets){
                        finishDialog()
                    } else {
                        nextSetDialog()
                    }
                }
            }
        }
        if (name.contains("Squat")){
            if (exerciseState=="stretch" && squatDownPosition(kneeLeft, kneeRight, hipLeft, hipRight)){
                return true
            }
        } else if (name.contains("Press")) { //done
            if (exerciseState == "stretch" && isUpPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight)){
                return true
            }
        } else if(name.contains("Pull Up")){ //done - need rep -1
            if (exerciseState=="stretch" && isDownPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight)){
                return true
            }
        } else if(name.contains("Bicep Curl")){ //done
            if (exerciseState=="stretch" && bicepDownPosition(wristLeft, wristRight, elbowLeft, elbowRight)){
                return true
            }
        } else if(name.contains("Row")) {
            if (exerciseState=="stretch" && rowDownPosition(elbowLeft, elbowRight, hipLeft, hipRight)){
                return true
            }
        } else if(name.contains("Push Up")) {
            if(exerciseState=="stretch" && isDownPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight)){
                return true
            }
        } else if(name.contains("Skullcrusher")) {
            if (exerciseState=="stretch" && bicepDownPosition(wristLeft, wristRight, elbowLeft, elbowRight)){
                return true
            }
        } else if(name.contains("Deadlift")) { //done need rep -1
            if (exerciseState=="stretch"&&deadliftDownPosition(wristLeft, wristRight, kneeLeft, kneeRight)){
                return true
            }
        }
        return false
    }
    private fun unbindCamera(){
        cameraProviderFuture?.addListener({
            try {
                val cameraProvider = cameraProviderFuture!!.get()
                cameraProvider.unbindAll()
            } catch (e: ExecutionException) {
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(this@ExerciseActivity))
    }

    private fun nextSetDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Finished")
            .setMessage("Would you like to continue to the next set?")
            .setPositiveButton("Continue"){dialog ,_ ->
                dialog.dismiss()
                inputDialog()
            }
            .setNegativeButton("Finish Exercise"){dialog ,_ ->
                dialog.dismiss()
                endActivity()
            }
        builder.setCancelable(false)
        builder.show()
    }

    private fun endActivity(){
        if (repList.isNotEmpty() && weightList.isNotEmpty()){
            historyViewModel.createHistoryDetail(name,repList,weightList)
            historyViewModel.historyDetail.observe(this@ExerciseActivity){
                if(it!=null){
                    val intent = Intent()
                    intent.putExtra("historyId",historyId)
                    intent.putExtra("detailId",it)
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }
        } else {
            val intent = Intent()
            intent.putExtra("historyId",historyId)
            intent.putExtra("detailId","null")
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    private fun inputDialog(){
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setTitle("Weight Input")
        builder.setView(input)
        builder.setMessage("Please input the weight")
        builder.setPositiveButton("Start"){dialog, which ->
            val weightInput = input.text.toString()
            if (weightInput.isNotEmpty()){
                val weightInInt = weightInput.toInt()
                if (weightInInt<=0){
                    Toast.makeText(this,"Enter a valid number", Toast.LENGTH_SHORT).show()
                    inputDialog()
                } else {
                    dialog.dismiss()
                    weight = weightInInt
                    currentSet++
                    exerciseCounter = 0
                    timer!!.start()
                }
            } else {
                dialog.dismiss()
                Toast.makeText(this,"Enter a number", Toast.LENGTH_SHORT).show()
                inputDialog()
            }
        }
        builder.setNegativeButton("Cancel"){ dialog, which ->
            dialog.dismiss()
            nextSetDialog()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun giveUpDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Give up")
            .setMessage("Would you like to finish the set?")
            .setPositiveButton("Yes"){dialog,_ ->
                if (exerciseCounter>0){
                    repList.add(exerciseCounter)
                    weightList.add(weight)
                }
                dialog.dismiss()
                if (currentSet>=sets){
                    dialog.dismiss()
                    finishDialog()
                } else {
                    dialog.dismiss()
                    nextSetDialog()
                }
            }
            .setNegativeButton("No"){dialog,_ ->
                dialog.dismiss()
                timer!!.start()
            }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun finishDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exercise Finished")
        builder.setMessage("Targeted set has been reached!")
            .setPositiveButton("Ok"){dialog, which ->
                dialog.dismiss()
                endActivity()
            }
        builder.setCancelable(false)
        builder.show()
    }

    private fun updateExerciseState(pose: Pose, name: String){
        val shoulderLeft = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val shoulderRight = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val elbowLeft = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val elbowRight = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val wristLeft = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val wristRight = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        val hipLeft = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val hipRight = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        val kneeLeft = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
        val kneeRight = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)
        if (name.contains("Squat")){
            if(squatUpPosition(kneeLeft, kneeRight, hipLeft, hipRight)){
                exerciseState = "stretch"
            } else if(squatDownPosition(kneeLeft, kneeRight, hipLeft, hipRight)){
                exerciseState = "squeeze"
            }
        } else if (name.contains("Press")) {
            if (isDownPosition(shoulderLeft,shoulderRight,elbowLeft, elbowRight))  {
                exerciseState = "stretch"
            } else if(isUpPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight) ){
                exerciseState = "squeeze"
            }
        } else if(name.contains("Pull Up")){
            if (isDownPosition(shoulderLeft,shoulderRight,elbowLeft, elbowRight))  {
                exerciseState = "squeeze"
            } else if(isUpPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight) ){
                exerciseState = "stretch"
            }
        } else if(name.contains("Bicep Curl")){
            if (bicepDownPosition(wristLeft,wristRight,elbowLeft,elbowRight)){
                exerciseState = "squeeze"
            } else if(bicepUpPosition(wristLeft,wristRight,elbowLeft,elbowRight)) {
                exerciseState = "stretch"
            }
        } else if(name.contains("Row")) {
            if(rowDownPosition(elbowLeft, elbowRight, hipLeft, hipRight)){
                exerciseState="squeeze"
            } else if(rowUpPosition(elbowLeft, elbowRight, hipLeft, hipRight)){
                exerciseState="stretch"
            }
        } else if(name.contains("Push Up")) {
            if (isDownPosition(shoulderLeft,shoulderRight,elbowLeft, elbowRight))  {
                exerciseState = "squeeze"
            } else if(isUpPosition(shoulderLeft, shoulderRight, elbowLeft, elbowRight) ){
                exerciseState = "stretch"
            }
        } else if(name.contains("Skullcrusher")) {
            if (bicepDownPosition(wristLeft,wristRight,elbowLeft,elbowRight)){
                exerciseState = "squeeze"
            } else if(bicepUpPosition(wristLeft,wristRight,elbowLeft,elbowRight)) {
                exerciseState = "stretch"
            }
        } else if(name.contains("Deadlift")) {
            if (deadliftDownPosition(wristLeft, wristRight, kneeLeft, kneeRight)){
                exerciseState = "squeeze"
            } else if(deadliftUpPosition(wristLeft, wristRight, kneeLeft, kneeRight)){
                exerciseState = "stretch"
            }
        }
        Log.i("Position",exerciseState)
    }
    private fun isUpPosition(shoulderLeft: PoseLandmark?, shoulderRight: PoseLandmark?, elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?): Boolean {
        return shoulderLeft != null
                && shoulderRight != null
                && elbowLeft != null
                && elbowRight != null
                && shoulderLeft.position.y > elbowLeft.position.y
                && shoulderRight.position.y > elbowRight.position.y
    }
    private fun rowDownPosition(elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?, hipLeft: PoseLandmark?, hipRight: PoseLandmark?):Boolean{
        return elbowLeft!=null && elbowRight!=null && hipLeft!=null && hipRight!=null && hipLeft.position.y > elbowLeft.position.y && hipRight.position.y > elbowRight.position.y
    }
    private fun rowUpPosition(elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?, hipLeft: PoseLandmark?, hipRight: PoseLandmark?):Boolean{
        return elbowLeft!=null && elbowRight!=null && hipLeft!=null && hipRight!=null && hipLeft.position.y < elbowLeft.position.y && hipRight.position.y < elbowRight.position.y
    }
    private fun bicepDownPosition(wristLeft: PoseLandmark?, wristRight: PoseLandmark?, elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?): Boolean{
        return wristRight!=null && wristLeft!=null && elbowLeft!=null && elbowRight!=null && wristLeft.position.y < elbowLeft.position.y && wristRight.position.y < elbowRight.position.y
    }
    private fun bicepUpPosition(wristLeft: PoseLandmark?, wristRight: PoseLandmark?, elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?): Boolean{
        return wristRight!=null && wristLeft!=null && elbowLeft!=null && elbowRight!=null && wristLeft.position.y > elbowLeft.position.y && wristRight.position.y > elbowRight.position.y
    }
    private fun deadliftDownPosition(wristLeft: PoseLandmark?, wristRight: PoseLandmark?, kneeLeft: PoseLandmark?, kneeRight: PoseLandmark?) : Boolean {
        return wristRight!=null && wristLeft!=null && kneeLeft!=null && kneeRight!=null && kneeLeft.position.y > wristLeft.position.y && kneeRight.position.y > wristRight.position.y
    }
    private fun deadliftUpPosition(wristLeft: PoseLandmark?, wristRight: PoseLandmark?, kneeLeft: PoseLandmark?, kneeRight: PoseLandmark?) : Boolean {
        return wristRight!=null && wristLeft!=null && kneeLeft!=null && kneeRight!=null && kneeLeft.position.y < wristLeft.position.y && kneeRight.position.y < wristRight.position.y
    }
    private fun squatDownPosition(kneeLeft: PoseLandmark?, kneeRight: PoseLandmark?, hipLeft: PoseLandmark?, hipRight: PoseLandmark?) : Boolean {
        return kneeRight!=null && kneeLeft!=null && hipLeft!=null && hipRight!=null && kneeLeft.position.y >= hipLeft.position.y && kneeRight.position.y >= hipRight.position.y
    }
    private fun squatUpPosition(kneeLeft: PoseLandmark?, kneeRight: PoseLandmark?, hipLeft: PoseLandmark?, hipRight: PoseLandmark?) : Boolean {
        return kneeRight!=null && kneeLeft!=null && hipLeft!=null && hipRight!=null && kneeLeft.position.y < hipLeft.position.y && kneeRight.position.y < hipRight.position.y
    }
    private fun isDownPosition(shoulderLeft: PoseLandmark?, shoulderRight: PoseLandmark?, elbowLeft: PoseLandmark?, elbowRight: PoseLandmark?): Boolean {
        return shoulderLeft != null
                && shoulderRight != null
                && elbowLeft != null
                && elbowRight != null
                && shoulderLeft.position.y < elbowLeft.position.y
                && shoulderRight.position.y < elbowRight.position.y
    }

    private val requiredPermissions: Array<String?>
        private get() = try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.size > 0) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }

    private fun allPermissionsGranted(): Boolean {
        for (permission in requiredPermissions) {
            if (!isPermissionGranted(this, permission!!)) {
                return false
            }
        }
        return true
    }

    private val runtimePermissions: Unit
        private get() {
            val allNeededPermissions: MutableList<String?> = ArrayList()
            for (permission in requiredPermissions) {
                if (!isPermissionGranted(this, permission!!)) {
                    allNeededPermissions.add(permission)
                }
            }
            if (!allNeededPermissions.isEmpty()) {
                ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
                )
            }
        }

    companion object {
        private fun isPermissionGranted(context: Context, permission: String): Boolean {
            return (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED)
        }
    }
}