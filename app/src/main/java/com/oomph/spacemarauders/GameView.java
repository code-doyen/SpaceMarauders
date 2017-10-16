package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by david on 9/18/2017.
 */

public class GameView extends SurfaceView implements Runnable {

    //context to be used in onTouchEvent to cause the activity transition from GameActivity to MainActivity.
    Context context;

    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread
    private Thread gameThread = null;

    //adding the player to this class
    private Player player;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Adding 30 enemies you may increase the size
    private int enemyCount = 30;

    //Adding enemies object array
    private Enemy[] enemies;

    // The player's bullet
    private Bullet bullet;

    // The enemies bullets
    private Bullet[] enemiesBullets = new Bullet[200];
    private int nextBullet;
    private int maxEnemiesBullets = 10;

    // The player's shelters are built from bricks
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    //created a reference of the class Friend
//    private Friend friend;

    //Adding an stars list
    private ArrayList<Stars> stars = new ArrayList<>();

    //defining a boom object to display blast
    private Boom boom;

    //a screenX/Y holder
    int screenX;
    int screenY;
    //to count the number of Misses
    int countMisses;

    //indicator that the enemy has just entered the game screen
    boolean flag ;

    //an indicator if the game is Over
    private boolean isGameOver ;

    //the score holder
    private int score;

    // Lives
    private int lives = 3;

    //the high Scores Holder
    int highScore[] = new int[4];

    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    //the mediaplayer objects to configure the background music
    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;
    final MediaPlayer shootSound;

    //Class constructor
    public GameView(Context context, int screenX, int screenY) {
        super(context);

        //initializing context
        this.context = context;

        //initializing player object
        //this time also passing screen size to player constructor
        player = new Player(context, screenX, screenY);

        // Prepare the players bullet
        bullet = new Bullet(context, screenX, screenY);
        // Initialize the enemiesBullets array
        for(int i=0; i < enemiesBullets.length; i++){
            enemiesBullets[i] = new Bullet(context, screenX, screenY);
        }
        //intializing enemy object array
        enemies = new Enemy[enemyCount];

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //adding 100 stars you may increase the number
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Stars s  = new Stars(screenX, screenY);
            stars.add(s);
        }

        //initializing enemy object array
        int numInvaders = 0;
        for(int column =0; column < 6; column++){
            for(int row = 0; row<5; row++){
                enemies[numInvaders] = new Enemy(context, row, column, screenX, screenY);
                numInvaders++;
            }
        }
        // Build the shelters
        numBricks = 0;
        for(int shelterNumber =0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 8; column++){
                for(int row = 0; row < 3; row++){
                    bricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
        // Reset the menace level
        //menaceInterval = 1000;
        //initializing boom object
        boom = new Boom(context);

        //initializing the Friend class object
        //friend = new Friend(context, screenX, screenY);

        //initializing screenX holder
        this.screenX = screenX;
        this.screenY = screenY;

        //initializing count the number of Misses
        countMisses = 0;

        //initializing indicator if the game is Over
        isGameOver = false;

        //setting the score to 0 initially
        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

        //initializing the media players for the game sounds
        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);
        shootSound = MediaPlayer.create(context, R.raw.shoot);
        //starting the game music as the game starts
        gameOnsound.start();
    }

    @Override
    public void run() {
        while (playing) {
            //to update the frame
            if(playing)
                update();

            //to draw the frame
            draw();

            //to control
            control();
        }
    }


    private void update() {
        // Did an invader bump into the side of the screen
        boolean bumped = false;

        //incrementing score as time passes
        score++;

        //updating player position
        player.update();

        // Update the players bullet
        if(bullet.getStatus()){
            bullet.update();
        }

        //setting boom outside the screenat
        boom.setX(-250);
        boom.setY(-250);

        //Updating the stars with player speed
        for (Stars s : stars) {
            s.update(player.getSpeed());
        }

        for(int i = 0; i < enemiesBullets.length; i++){
            // Update all the invaders bullets if active
            if(enemiesBullets[i].getStatus()) {
                enemiesBullets[i].update();

                for(int j=0; j< numBricks; j++){
                    // Has an alien bullet hit a shelter brick
                    if(bricks[j].getInvisibility()){
                        if(Rect.intersects(enemiesBullets[i].getDetectCollision(), bricks[j].getRect())){
                            //A collision has occured
                            enemiesBullets[i].setInactive();
                            bricks[j].setInvisible();
                            //soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                        }
                        // Has a player bullet hit a shelter brick
                        if(bullet.getStatus()) {
                            if (Rect.intersects(bullet.getDetectCollision(), bricks[j].getRect())) {
                                // a collision has occured
                                bullet.setInactive();
                                bricks[j].setInvisible();
                          //      soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                            }
                        }
                    }
                }
            }
            // Has an invaders bullet hit the bottom of the screen
            if(enemiesBullets[i].getImpactPointY() >screenY){
                enemiesBullets[i].setInactive();
            }
        }
        // Has a player bullet hit a shelter brick
        if(bullet.getStatus()){
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getInvisibility()){
                    if(Rect.intersects(bullet.getDetectCollision(), bricks[i].getRect())){
                        // a collision has occured
                        bullet.setInactive();
                        bricks[i].setInvisible();
                     //   soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }
        // Update the invaders if visible
        for(int i = 0; i < enemyCount; i++){
            if(enemies[i].getVisibility()){
                //move the next invader
                enemies[i].update();
                //attempt a shot
                if(enemies[i].takeAim(player.getX(), player.getLength())){
                    //try to spawn bullet
                    if(enemiesBullets[nextBullet].shoot(enemies[i].getX() + enemies[i].getlength() /2,
                            enemies[i].getY(), bullet.DOWN)){
                        //Shot fired, prepare next shot
                        nextBullet++;
                        //loop back to the first one if we have reached the last
                        if(nextBullet == maxEnemiesBullets){
                            //This stops the firing of another bullet until one completes its journey
                            //Because if bullet 0 is still alive active shoot returns false
                            nextBullet = 0;
                        }
                    }
                }
                // If move cause them to bump the screen change bumped to true
                if(enemies[i].getX() > screenX - enemies[i].getlength() || enemies[i].getX() < 0){
                    bumped = true;
                }
            }
        }
        // Did an invader bump into the edge of the screen
        if(bumped){
            //move all the invaders down and change direction
            for(int i = 0; i < enemyCount; i++){
                enemies[i].dropDownAndReverse();
                //have the invaders landed
                if(enemies[i].getY() > screenY - screenY /10){
                    isGameOver = true;
                }
            }
            //increase the menace sound level by making the sounds more frequent
//            menaceInterval -= 80;
        }

//        if(lost){
//            prepareLevel();
//        }

        // Has the player's bullet hit the top of the screen
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        // Has the player's bullet hit an invader
        if(bullet.getStatus()){
            for(int i = 0; i < enemyCount; i++){
                if(enemies[i].getVisibility()){
                    if(Rect.intersects(bullet.getDetectCollision(), enemies[i].getDetectCollision())){
                        enemies[i].setInvisible();
                      //  soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        score += 10;
                        //has player won
//                        if(score == numInvaders * 10){
//                            paused =true;
//                            score = 0;
//                            lives =3;
//                            prepareLevel();
//                        }
                    }
                }
            }
        }


        // Has an invader bullet hit the player ship buggy!!!!
        for(int i =0; i < enemiesBullets.length; i++){
            if(enemiesBullets[i].getStatus()){
                if(Rect.intersects(player.getDetectCollision(),enemiesBullets[i].getDetectCollision())){
                    enemiesBullets[i].setInactive();
                    //displaying boom at that location
                    boom.setX(enemies[i].getX());
                    boom.setY(enemies[i].getY());
                    //will play a sound at the collision between player and the enemy
                    killedEnemysound.start();
                    lives--;
                   // soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
                    // is the game over
//                    if(lives == 0){
//                        paused = true;
//                        lives =3;
//                        score = 0;
//                        prepareLevel();
//                    }
                }
            }
        }
//           // the condition where player misses the enemy
//            else {
//                //stopping the gameon music
//                gameOnsound.stop();
//                //play the game over sound
//                gameOversound.start();
//
//                //Assigning the scores to the highscore integer array
//                for (int j = 0; j < 4; j++) {
//                    if (highScore[j] < score) {
//
//                        final int finalI = j;
//                        highScore[j] = score;
//                        break;
//                    }
//                }
//
//                //storing the scores through shared Preferences
//                SharedPreferences.Editor e = sharedPreferences.edit();
//                for (int k = 0; k < 4; k++) {
//                    int j = k + 1;
//                    e.putInt("score" + j, highScore[k]);
//                }
//                e.apply();
//            }
    }

    private void draw() {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);

            //drawing all stars
            for (Stars s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //drawing the score on the game screen
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);

            //Drawing the player
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            //drawing the enemies
            for (int i = 0; i < enemyCount; i++) {
                if(enemies[i].getVisibility()){
                    //if(uhOrOh)
                        canvas.drawBitmap(enemies[i].getBitmap(), enemies[i].getX(), enemies[i].getY(), paint);
                    //else
                    //  canvas.drawBitmap(enemies[i].getBitmap2(), enemies[i].getX(), enemies[i].getY(), paint);
                }
            }
            // Draw the bricks if visible
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getInvisibility()){
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            // Draw the players bullet if active
            if(bullet.getStatus()){
                //canvas.drawRect(bullet.getRect(), paint);
                canvas.drawBitmap(bullet.getBitmap(), bullet.getX(), bullet.getY(), paint);
            }

            // Draw the invaders bullets if active
            for(int i = 0; i < enemiesBullets.length; i++){
                if(enemiesBullets[i].getStatus()){
                    //canvas.drawRect(invadersBullets[i].getRect(), paint);
                    canvas.drawBitmap(enemiesBullets[i].getBitmap(), enemiesBullets[i].getX(), enemiesBullets[i].getY(), paint);
                }
            }

            // Change the brush color, Draw the score and remaining lives
            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);
            //drawing boom image
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //drawing friends image
//            canvas.drawBitmap(
//
//                    friend.getBitmap(),
//                    friend.getX(),
//                    friend.getY(),
//                    paint
//            );

            //draw game Over when the game is over
            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    //stop the music on exit
    public static void stopMusic(){
        gameOnsound.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //stopping the boosting when screen is released
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //boosting the space jet when screen is pressed
                player.setBoosting();
                //player's shot(s) fired
                if(bullet.shoot(player.getX()+player.getLength()/2, screenY, bullet.UP)){
                  //  soundPool.play(shootID, 1, 1, 0, 0, 1);
                    shootSound.start();
                }
                break;
        }
        //if the game's over, tappin on game Over screen sends you to MainActivity
        if(isGameOver){

            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){

                context.startActivity(new Intent(context,MainActivity.class));

            }

        }

        return true;

    }

}
