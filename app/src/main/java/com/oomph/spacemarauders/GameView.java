package com.oomph.spacemarauders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;

/**
 * Created by david on 9/18/2017.
 */

public class GameView extends SurfaceView implements Runnable {
    public static final String EXTRA_MESSAGE = "com.oomph.spacemarauders.MESSAGE";
    private int nameScorePos;
    //context to be used in onTouchEvent to cause the activity transition from GameActivity to MainActivity.
    Context context;

    //boolean variable to track if the game is playing or not
    volatile boolean gameRunning;

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
    private int maxEnemiesBullets = 5;
    //Adding enemies object array
    private Enemy[] enemies;

    // The player's bullet
    private Bullet bullet;

    // The enemies bullets
    private Bullet[] enemiesBullets = new Bullet[maxEnemiesBullets];
    private int nextBullet;


    // The player's shelters are built from bricks
    private DefenceBrick[] bricks = new DefenceBrick[100];
    private int numBricks;

    //created a reference of the class Friend
//    private Friend friend;

    //adding stars you may increase the number
    private int numStars = 100;

    //Adding an stars list
    private ArrayList<Stars> stars = new ArrayList<>();

    //defining a boom object to display blast
    private Explosion boom;

    //a screenX/Y holder
    int screenX;
    int screenY;
    //to count the number of Misses
    int countMisses;

    // This variable tracks the game frame rate
    private long fps = 60;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // How menacing should the sound be?
    private long menaceInterval;

    // Which menace sound should play next
    private boolean uhOrOh;

    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();

    //an indicator if the game is Over
    private boolean isGameOver;

    //Marauder has struck ship
    private boolean hasLanded;

    //the score holder
    private int score;

    // Lives
    private int lives;
    //Getting bitmap from drawable resource
    private Bitmap imgLives;
    //Has a life been lost
    private boolean lifeLost;

    //Spawn point intial time
    private long spawnStartTime;
    private long spawnLapsedTime;

    //the high Scores Holder
    int highScore[] = new int[4];
    String highScoreNames[] = new String[4];
    boolean isHighScore;
    //Shared Prefernces to store the High Scores
    SharedPreferences sharedPreferences;

    //the mediaplayer objects to configure the background music
    static MediaPlayer level1Sound;
    final MediaPlayer killedEnemySound;
    final MediaPlayer playerKilledSound;
    final MediaPlayer gameOverSound;
    final MediaPlayer shootSound;
    final MediaPlayer damageShelterSound;
    final MediaPlayer uhSound;
    final MediaPlayer ohSound;


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
        for (int i = 0; i < enemiesBullets.length; i++) {
            enemiesBullets[i] = new Bullet(context, screenX, screenY);
        }
        //initializing enemy object array
        enemies = new Enemy[enemyCount];

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //adding stars you may increase the number
        for (int i = 0; i < numStars; i++) {
            Stars s = new Stars(screenX, screenY);
            stars.add(s);
        }

        //initializing enemy object array
        int numInvaders = 0;
        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 5; row++) {
                enemies[numInvaders] = new Enemy(context, row, column, screenX, screenY);
                numInvaders++;
            }
        }
        // Build the shelters
        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 5; shelterNumber++) {
            for (int column = 0; column < 4; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenceBrick(context, row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
        // Reset the menace level
        menaceInterval = 1999;

        //initializing boom object
        boom = new Explosion(context, screenX, screenY);

        //initializing the Friend class object
        //friend = new Friend(context, screenX, screenY);

        //initializing screenX holder
        this.screenX = screenX;
        this.screenY = screenY;

        //initializing count the number of Misses
        countMisses = 0;

        //initializing indicators if the game is Over
        isGameOver = false;
        hasLanded = false;

        //initialize lives
        lives = 3;
        imgLives = BitmapFactory.decodeResource(context.getResources(), R.drawable.mship1);
        //stretch the bitmap to a size appropriate for the screen resolution
        imgLives = Bitmap.createScaledBitmap(imgLives, screenX/30, screenY/30,false);

        //initialize life lost
        lifeLost = false;

        //initialize ship spawning time
        spawnStartTime = System.currentTimeMillis();

        //setting the score to 0 initially
        score = 0;

        //load high score sna names from file system
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //initializing the array high scores with the previous values
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);
        highScoreNames[0] = sharedPreferences.getString("scoreName1", "DBV");
        highScoreNames[1] = sharedPreferences.getString("scoreName2", "MAV");
        highScoreNames[2] = sharedPreferences.getString("scoreName3", "HMV");
        highScoreNames[3] = sharedPreferences.getString("scoreName4", "BLV");
        isHighScore = false;

        //initializing the media players for the game sounds
        level1Sound = MediaPlayer.create(context, R.raw.battleinthestars);
        killedEnemySound = MediaPlayer.create(context, R.raw.killedenemy);
        playerKilledSound = MediaPlayer.create(context, R.raw.playerexplode);
        gameOverSound = MediaPlayer.create(context, R.raw.gameovertune);
        shootSound = MediaPlayer.create(context, R.raw.playershot);
        damageShelterSound = MediaPlayer.create(context, R.raw.damageshelter);
        uhSound = MediaPlayer.create(context, R.raw.uh);
        ohSound = MediaPlayer.create(context, R.raw.oh);

    }

    @Override
    public void run() {
        while (gameRunning) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            //to update the frame
            if (gameRunning)
                update();

            //to draw the frame
            draw();

            // Calculate the fps this frame; We can then use the result on time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
            if (!isGameOver) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        //play uh
                        ohSound.start();
                        //soundPool.play(uhID, 1, 1, 0, 0, 1);
                    } else {
                        //play oh
                        ohSound.start();
                        //soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }
                    lastMenaceTime = System.currentTimeMillis();
                    uhOrOh = !uhOrOh;
                }
            }
            //to control
            control();
        }
    }


    private void update() {
        // Did an invader bump into the side of the screen
        boolean bumped = false;

        //updating player position
        if (!lifeLost)
            player.update(fps);

        // Update the players bullet
        if (bullet.getStatus()) {
            bullet.update(fps);
        }

        //Updating the stars with player speed
        for (Stars s : stars) {
            s.update(menaceInterval / fps);
        }
        
        //Updating all active bullets
        for (Bullet be : enemiesBullets) {
            // Update the enemies bullets if active
            if(be.getStatus()){
                be.update(fps);
                if(RectF.intersects(player.getDetectCollision(), be.getDetectCollision())){
                    if(!lifeLost && !isGameOver) {
                        be.setInactive();
                        //displaying boom at that location
                        boom.setCoordinates(player.getX(), player.getY());
                        boom.setVisible();
                        //will play a sound at the collision between player and the enemy
                        playerKilledSound.start();
                        lives--;
                    }
                    // soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);
                    // is the game over
                    if(lives == 0){
                        isGameOver = true;
                        endGame();
                    }
                    if(lives > 0)
                    {
                        spawnStartTime = System.currentTimeMillis(); //initialize ship time of death
                        lifeLost = true;
                    }
                    break;
                }
            }
            for (DefenceBrick br : bricks) {
                // Has an alien bullet hit a shelter brick
                if (br.isVisible() && RectF.intersects(be.getDetectCollision(), br.getDetectCollision())) {
                    Log.i(getClass().getName(), " Alien bullet colliding brick now");
                    //A collision has occurred
                    be.setInactive();
                    br.setInvisible();
                    //displaying boom at that location
                    boom.setCoordinates(be.getX(), be.getY());
                    boom.setVisible();
                    damageShelterSound.start();
                    //soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                }
                
                // Has a player bullet hit a shelter brick
                if (bullet.getStatus()) {
                    if (br.isVisible() && RectF.intersects(bullet.getDetectCollision(), br.getDetectCollision())) {
                        Log.i(getClass().getName(), " Player bullet colliding brick now");
                        // a collision has occurred
                        bullet.setInactive();
                        br.setInvisible();
                        //displaying boom at that location
                        boom.setCoordinates(bullet.getX(), bullet.getY());
                        boom.setVisible();
                        damageShelterSound.start();
                        //soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
            // Has an invaders bullet hit the bottom of the screen
            if (be.getImpactPointY() > screenY) {
                be.setInactive();
            }
        }

        // Update the invaders if visible
        for (Enemy es : enemies) {
            // Has the player's bullet hit the top of the screen
            if (bullet.getImpactPointY() < 0) {
                bullet.setInactive();
            }
            // Has the player's bullet hit an invader
            if (bullet.getStatus()) {
                if (es.isVisible()) {
                    if (RectF.intersects(bullet.getDetectCollision(), es.getDetectCollision())) {
                        es.setInvisible();
                        //displaying boom at that location
                        boom.setCoordinates(es.getX(), es.getY());
                        boom.setVisible();
                        //will play a sound at the collision between player and the enemy
                        killedEnemySound.start();
                        //  soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        score += 10;
                        //has player won
//                    if(score == numInvaders * 10){
//                        paused =true;
//                        score = 0;
//                        lives =3;
//                        prepareLevel();
//                    }
                    }
                }
            }
            if (es.isVisible()) {
                //move the next invader
                es.update(fps);
                //attempt a shot
                if (!lifeLost && es.takeAim(player.getX(), player.getLength())) {
                    //try to spawn bullet
                    if (enemiesBullets[nextBullet].shoot(es.getX() + (es.getLength() / 2) - (bullet.getLength() / 2), es.getY(), bullet.DOWN)) {
                        //Shot fired, prepare next shot
                        nextBullet++;
                        //loop back to the first one if we have reached the last
                        if (nextBullet == maxEnemiesBullets) {
                            //This stops the firing of another bullet until one completes its journey
                            //Because if bullet 0 is still alive active shoot returns false
                            nextBullet = 0;
                        }
                    }
                }
                //Has an alien ship hit a shelter brick
                for (DefenceBrick br : bricks) {
                    if (br.isVisible()){
                        if (RectF.intersects(br.getDetectCollision(), es.getDetectCollision())) {
                            isGameOver = true;
                            lives = 0;
                            endGame();
                        }
                    }
                }
                //Has an alien ship struck player
                if (RectF.intersects(player.getDetectCollision(), es.getDetectCollision())) {
                    isGameOver = true;
                    lives = 0;
                    hasLanded = true;
                    endGame();
                }
                // If move cause them to bump the screen change bumped to true
                if (es.getX() > screenX - es.getLength() || es.getX() < 0) {
                    bumped = true;
                }
            }
        }
        // Did an invader bump into the edge of the screen
        if (bumped) {
            //move all the invaders down and change direction
            for (Enemy es : enemies) {
                es.dropDownAndReverse(fps);
            }
            //increase the menace sound level by making the sounds more frequent
            if(menaceInterval > 100)
                menaceInterval -= 80;
        }
    }

    private void endGame() {
        level1Sound.stop();
        gameOverSound.start();
        bullet.setInactive();
        for (Bullet be : enemiesBullets) {
            be.setInactive();
        }
        for (Enemy es : enemies) {
            es.setInvisible();
        }
        //Assigning the scores to the highscore integer array
        for (int j = 0; j < highScore.length; j++) {
            int temp;
            String nemp;
            if (highScore[j] < score) {
                temp = highScore[j];
                nemp = highScoreNames[j];
                highScore[j] = score;
                score = temp;
                if (!isHighScore) {
                    nameScorePos = j;
                    isHighScore = true;
                    highScoreNames[j] = "";
                } else highScoreNames[j] = nemp;

            }
        }

        //storing the scores through shared Preferences
        SharedPreferences.Editor e = sharedPreferences.edit();
        for (int k = 0; k < highScore.length; k++) {
            int j = k + 1;
            e.putString("scoreName" + j, highScoreNames[k]);
            e.putInt("score" + j, highScore[k]);
        }
        e.apply();
    }

    private void prepareLevel() {
    }

    private void draw() {

        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid()) {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);

            //drawing all stars
            for (Stars s : stars) {
                //setting the paint color to draw the stars
                paint.setColor(s.getColor());
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //Drawing the player
            if (!lifeLost && !isGameOver) {
                canvas.drawBitmap(
                        player.getBitmap(),
                        player.getX(),
                        player.getY(),
                        paint);
            }


            //drawing the enemies
            for (Enemy es : enemies) {
                if (es.isVisible()) {
                    if (uhOrOh)
                        canvas.drawBitmap(es.getBitmap(), es.getX(), es.getY(), paint);
                    else
                        canvas.drawBitmap(es.getBitmap2(), es.getX(), es.getY(), paint);
                }
            }

            // Draw the bricks if visible
            for (DefenceBrick br : bricks) {
                if (br.isVisible()) {
                    canvas.drawBitmap(br.getBitmap(), br.getX(), br.getY(), paint);
                }
            }
            // Draw the players bullet if active
            if (bullet.getStatus()) {
                //canvas.drawRect(bullet.getRect(), paint);
                canvas.drawBitmap(bullet.getBitmap(), bullet.getX(), bullet.getY(), paint);
            }

            // Draw the invaders bullets if active
            for (Bullet be : enemiesBullets) {
                if (be.getStatus()) {
                    //canvas.drawRect(invadersBullets[i].getRect(), paint);
                    canvas.drawBitmap(be.getBitmap(), be.getX(), be.getY(), paint);
                }
            }

            //drawing boom image
            if (boom.isVisible()) {
                canvas.drawBitmap(
                        boom.getBitmap(),
                        boom.getX(),
                        boom.getY(),
                        paint
                );
                boom.setVisible();
            }
            //drawing friends image
//            canvas.drawBitmap(
//
//                    friend.getBitmap(),
//                    friend.getX(),
//                    friend.getY(),
//                    paint
//            );
            // Change the brush color, Draw the score and remaining lives
            for(int c = 1; c <= lives; c++) {
                canvas.drawBitmap(
                        imgLives,
                        (float) (screenX - ((30+screenX / 60) * c)),
                        (float) (screenY / 60),
                        paint
                );
            }
            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(40);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score: " + score, 100, screenY / 21.6f, paint);//y=50

            //draw game Over when the game is over
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            if (isGameOver) {
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);

            }
            if(hasLanded){
                paint.setTextSize(50);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Marauders Landed", canvas.getWidth() / 2, yPos + 100, paint);
            }

            if (lifeLost) {
                for (Bullet be : enemiesBullets) {
                    be.setInactive();
                }
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("READY", canvas.getWidth() / 2, yPos, paint);
                //determine time since ship expired or intitially spawned
                spawnLapsedTime = System.currentTimeMillis();
                if (spawnLapsedTime - spawnStartTime > 5000) {
                    player = new Player(context, screenX, screenY);
                    lifeLost = false;
                }

            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        //when the game is paused
        //setting the variable to false
        level1Sound.pause();
        gameRunning = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public void resume() {
        //loop game soundtrack
        level1Sound.setLooping(true);
        //starting the game music as the game starts
        level1Sound.start();
        //when the game is resumed
        //starting the thread again
        gameRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //When the user presses on the screen
                //stopping the boosting when screen is released
                player.setMovement(player.NEUTRAL);
                break;
            case MotionEvent.ACTION_DOWN:
                //When the user releases the screen
                //set the space jet movement when screen is pressed
                if(motionEvent.getX() > screenX/2) {
                    player.setMovement(player.RIGHT);
                    Log.i(getClass().getName(), "d: RIGHT");
                }
                else if (motionEvent.getX() < screenX/2) {
                    player.setMovement(player.LEFT);
                    Log.i(getClass().getName(), "d: LEFT");
                }

                Log.i(getClass().getName(), "Loc: "+String.valueOf(motionEvent.getX()));
                //player's shot(s) fired
                if (!lifeLost && !isGameOver && bullet.shoot(player.getX() + (player.getLength() / 2) - (bullet.getLength() / 2), player.getY(), bullet.UP)) {
                    //  soundPool.play(shootID, 1, 1, 0, 0, 1);
                    shootSound.start();
                }
                break;
        }
        //if the game's over, tapping on game Over screen sends you to MainActivity
        if (isGameOver) {
            if (isHighScore && !gameOverSound.isPlaying()) {
                context.startActivity(new Intent(context, SaveScoreActivity.class).putExtra(EXTRA_MESSAGE, nameScorePos));

            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !gameOverSound.isPlaying()) {
                context.startActivity(new Intent(context, MainActivity.class));

            }
            else Log.i(getClass().getName(), " Failed to load intent and start activity");

        }

        return true;

    }


}
