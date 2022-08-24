package org.mario.ai.engine.core;

import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;

import org.mario.ai.agents.human.Agent;
import org.mario.ai.engine.helper.GameStatus;
import org.mario.ai.engine.helper.MarioActions;

public class MarioGame {
    /**
     * the maximum time that agent takes for each step
     */
    public static final long maxTime = 40;
    /**
     * extra time before reporting that the agent is taking more time that it should
     */
    public static final long graceTime = 10;
    /**
     * Screen width
     */
    public static final int width = 256;
    /**
     * Screen height
     */
    public static final int height = 256;
    /**
     * Screen width in tiles
     */
    public static final int tileWidth = width / 16;
    /**
     * Screen height in tiles
     */
    public static final int tileHeight = height / 16;
    /**
     * print debug details
     */
    public static final boolean verbose = false;

    /**
     * pauses the whole game at any moment
     */
    public boolean pause = false;

    /**
     * events that kills the player when it happens only care about type and param
     */
    private MarioEvent[] killEvents;

    // visualization
    private JFrame window = null;
    private JEditorPane metricas = null;
    private JEditorPane grid = null;
    private MarioRender render = null;
    private MarioAgent agent = null;
    private MarioWorld world = null;
    private MarioResult result = new MarioResult(world, new ArrayList<>(), new ArrayList<>());
    private TestPane gridColor = null;

    /**
     * Create a mario game to be played
     */
    public MarioGame() {

    }

    /**
     * Create a mario game with a different forward model where the player on
     * certain event
     *
     * @param killEvents events that will kill the player
     */
    public MarioGame(MarioEvent[] killEvents) {
        this.killEvents = killEvents;
    }

    private int getDelay(int fps) {
        if (fps <= 0) {
            return 0;
        }
        return 1000 / fps;
    }

    private void setAgent(MarioAgent agent) {
        this.agent = agent;
        if (agent instanceof KeyAdapter) {
            this.render.addKeyListener((KeyAdapter) this.agent);
        }
    }

    /**
     * Play a certain mario level
     *
     * @param level a string that constitutes the mario level, it uses the same
     *              representation as the VGLC but with more details. for more
     *              details about each symbol check the json file in the levels
     *              folder.
     * @param timer number of ticks for that level to be played. Setting timer to
     *              anything &lt;=0 will make the time infinite
     * @return statistics about the current game
     */
    public MarioResult playGame(String level, int timer) {
        return this.runGame(new Agent(), level, timer, 0, true, 30, 2);
    }

    /**
     * Play a certain mario level
     *
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @return statistics about the current game
     */
    public MarioResult playGame(String level, int timer, int marioState) {
        return this.runGame(new Agent(), level, timer, marioState, true, 30, 2);
    }

    /**
     * Play a certain mario level
     *
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @param fps        the number of frames per second that the update function is
     *                   following
     * @return statistics about the current game
     */
    public MarioResult playGame(String level, int timer, int marioState, int fps) {
        return this.runGame(new Agent(), level, timer, marioState, true, fps, 2);
    }

    /**
     * Play a certain mario level
     *
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @param fps        the number of frames per second that the update function is
     *                   following
     * @param scale      the screen scale, that scale value is multiplied by the
     *                   actual width and height
     * @return statistics about the current game
     */
    public MarioResult playGame(String level, int timer, int marioState, int fps, float scale) {
        return this.runGame(new Agent(), level, timer, marioState, true, fps, scale);
    }

    /**
     * Run a certain mario level with a certain agent
     *
     * @param agent the current AI agent used to play the game
     * @param level a string that constitutes the mario level, it uses the same
     *              representation as the VGLC but with more details. for more
     *              details about each symbol check the json file in the levels
     *              folder.
     * @param timer number of ticks for that level to be played. Setting timer to
     *              anything &lt;=0 will make the time infinite
     * @return statistics about the current game
     */
    public MarioResult runGame(MarioAgent agent, String level, int timer) {
        return this.runGame(agent, level, timer, 0, false, 0, 2);
    }

    /**
     * Run a certain mario level with a certain agent
     *
     * @param agent      the current AI agent used to play the game
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @return statistics about the current game
     */
    public MarioResult runGame(MarioAgent agent, String level, int timer, int marioState) {
        return this.runGame(agent, level, timer, marioState, false, 0, 2);
    }

    /**
     * Run a certain mario level with a certain agent
     *
     * @param agent      the current AI agent used to play the game
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @param visuals    show the game visuals if it is true and false otherwise
     * @return statistics about the current game
     */
    public MarioResult runGame(MarioAgent agent, String level, int timer, int marioState, boolean visuals) {
        return this.runGame(agent, level, timer, marioState, visuals, visuals ? 30 : 0, 2);
    }

    /**
     * Run a certain mario level with a certain agent
     *
     * @param agent      the current AI agent used to play the game
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @param visuals    show the game visuals if it is true and false otherwise
     * @param fps        the number of frames per second that the update function is
     *                   following
     * @return statistics about the current game
     */
    public MarioResult runGame(MarioAgent agent, String level, int timer, int marioState, boolean visuals, int fps) {
        return this.runGame(agent, level, timer, marioState, visuals, fps, 2);
    }

    public class TestPane extends JPanel {

        public TestPane() {
            setLayout(new GridLayout(16, 16, 1, 1));
            for (int col = 0; col < 16; col++) {
                for (int row = 0; row < 16; row++) {
                    JPanel cell = new JPanel();
                    add(cell);
                }
            }
        }

    }

    /**
     * Run a certain mario level with a certain agent
     *
     * @param agent      the current AI agent used to play the game
     * @param level      a string that constitutes the mario level, it uses the same
     *                   representation as the VGLC but with more details. for more
     *                   details about each symbol check the json file in the levels
     *                   folder.
     * @param timer      number of ticks for that level to be played. Setting timer
     *                   to anything &lt;=0 will make the time infinite
     * @param marioState the initial state that mario appears in. 0 small mario, 1
     *                   large mario, and 2 fire mario.
     * @param visuals    show the game visuals if it is true and false otherwise
     * @param fps        the number of frames per second that the update function is
     *                   following
     * @param scale      the screen scale, that scale value is multiplied by the
     *                   actual width and height
     * @return statistics about the current game
     */
    public MarioResult runGame(MarioAgent agent, String level, int timer, int marioState, boolean visuals, int fps,
            float scale) {

        if (visuals) {
            this.window = new JFrame("Super Mario Bot");
            this.render = new MarioRender(scale * 0.87f);

            this.window.setLayout(new GridLayout(2, 2));

            this.window.add(this.render);
            this.metricas = new JEditorPane();
            metricas.setFocusable(false);
            metricas.setEditable(false);
            this.grid = new JEditorPane();
            grid.setFocusable(false);
            this.gridColor = new TestPane();
            window.add(gridColor);
            window.add(metricas);
            window.add(grid);

            this.window.pack();
            this.window.setResizable(true);
            this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.render.init();

            this.window.setVisible(true);
        }
        this.setAgent(agent);

        return this.gameLoop(level, timer, marioState, visuals, fps, metricas);
    }

    private MarioResult gameLoop(String level, int timer, int marioState, boolean visual, int fps,
            JEditorPane metricas) {
        this.world = new MarioWorld(this.killEvents);
        this.world.visuals = visual;
        this.world.initializeLevel(level, 1000 * timer);
        if (visual) {
            this.world.initializeVisuals(this.render.getGraphicsConfiguration());
        }
        this.world.mario.isLarge = marioState > 0;
        this.world.mario.isFire = marioState > 1;
        this.world.update(new boolean[MarioActions.numberOfActions()]);
        long currentTime = System.currentTimeMillis();

        // initialize graphics
        VolatileImage renderTarget = null;
        Graphics backBuffer = null;
        Graphics currentBuffer = null;
        if (visual) {
            renderTarget = this.render.createVolatileImage(MarioGame.width, MarioGame.height);
            backBuffer = this.render.getGraphics();
            currentBuffer = renderTarget.getGraphics();
            this.render.addFocusListener(this.render);
        }

        MarioTimer agentTimer = new MarioTimer(MarioGame.maxTime);
        this.agent.initialize(new MarioForwardModel(this.world.clone()), agentTimer);

        ArrayList<MarioEvent> gameEvents = new ArrayList<>();
        ArrayList<MarioAgentEvent> agentEvents = new ArrayList<>();
        while (this.world.gameStatus == GameStatus.RUNNING) {
            if (visual) {
                updateGrid(world);
                updateGridColor(world);
                this.result.setWorld(world);
                this.result.setGameEvents(gameEvents);
                this.result.setAgentEvents(agentEvents);
                updateMetrics(this.result);
            }

            if (!this.pause) {
                // get actions
                agentTimer = new MarioTimer(MarioGame.maxTime);
                boolean[] actions = this.agent.getActions(new MarioForwardModel(this.world.clone()), agentTimer);
                if (MarioGame.verbose) {
                    if (agentTimer.getRemainingTime() < 0
                            && Math.abs(agentTimer.getRemainingTime()) > MarioGame.graceTime) {
                        System.out.println("The Agent is slowing down the game by: "
                                + Math.abs(agentTimer.getRemainingTime()) + " msec.");
                    }
                }
                // update world
                this.world.update(actions);
                gameEvents.addAll(this.world.lastFrameEvents);
                agentEvents.add(new MarioAgentEvent(actions, this.world.mario.x,
                        this.world.mario.y, (this.world.mario.isLarge ? 1 : 0) + (this.world.mario.isFire ? 1 : 0),
                        this.world.mario.onGround, this.world.currentTick));
            }

            // render world
            if (visual) {
                this.render.renderWorld(this.world, renderTarget, backBuffer, currentBuffer);
            }
            // check if delay needed
            if (this.getDelay(fps) > 0) {
                try {
                    currentTime += this.getDelay(fps);
                    Thread.sleep(Math.max(0, currentTime - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        MarioResult result = new MarioResult(this.world, gameEvents, agentEvents);
        if (visual) {
            updateGrid(world);
            updateMetrics(result);
        }

        return result;
    }

    private void updateMetrics(MarioResult result) {
        metricas.setText("Game Status: " + result.getGameStatus().toString() +
                "\nPercentage Completion: " + String.valueOf((int) Math.ceil(result.getCompletionPercentage() * 100))
                + "%" + "\nLives: "
                + String.valueOf(result.getCurrentLives()) + "\nCoins: " + String.valueOf(result.getCurrentCoins()) +
                "\nRemaining Time: " + String.valueOf(result.getRemainingTime() / 1000) + "\nMario State: "
                + String.valueOf(result.getMarioMode()) +
                "\nMushrooms: " + String.valueOf(result.getNumCollectedMushrooms()) + "\nFire Flowers: "
                + String.valueOf(result.getNumCollectedFireflower()) + "\nTotal Kills: "
                + String.valueOf(result.getKillsTotal()) + "\nStomps: " + String.valueOf(result.getKillsByStomp()) +
                "\nFireballs: " + String.valueOf(result.getKillsByFire()) + "\nShells: "
                + String.valueOf(result.getKillsByShell()) +
                "\nFalls: " + String.valueOf(result.getKillsByFall()) + "\nBricks: "
                + String.valueOf(result.getNumDestroyedBricks()) + "\nJumps: "
                + String.valueOf(result.getNumJumps()) +
                "\nMax X Jump: " + String.valueOf(result.getMaxXJump()) + "\nMax Air Time: "
                + String.valueOf(result.getMaxJumpAirTime()));
    }

    private void updateGrid(MarioWorld world) {
        int[][] scene = world.getMergedObservation(this.world.mario.x,
                140, 1, 1);
        this.grid
                .setText("\n\n\n" + Arrays.deepToString(transposeMatrix(scene)).replace("], ", "]\n").replace("0", "00")
                        .replace("[", "").replace("]", ""));
    }

    private void updateGridColor(MarioWorld world) {
        int[][] scene = world.getMergedObservation(this.world.mario.x,
                140, 2, 2);
        int[][] transpose = transposeMatrix(scene);
        for (int col = 0; col < transpose.length; col++) {
            for (int row = 0; row < transpose.length; row++) {
                int value = transpose[col][row];
                if (value == 100) {
                    this.gridColor.getComponent(row + 16 * col).setBackground(new Color(139, 69, 19));
                } else if (value == 1) {
                    this.gridColor.getComponent(row + 16 * col).setBackground(Color.red);
                } else {
                    this.gridColor.getComponent(row + 16 * col).setBackground(Color.WHITE);
                }
            }
        }
        if (world.mario.getMapX() < 8) {
            this.gridColor.getComponent(world.mario.getMapX() + 16 * world.mario.getMapY()).setBackground(Color.blue);
        } else {
            this.gridColor.getComponent(8 + 16 * world.mario.getMapY()).setBackground(Color.blue);
            if (world.mario.isLarge) {
                this.gridColor.getComponent(8 + 16 * (world.mario.getMapY() - 1)).setBackground(Color.blue);
            }
        }
    }

    public static int[][] transposeMatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        int[][] transposedMatrix = new int[n][m];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                transposedMatrix[x][y] = matrix[y][x];
            }
        }

        return transposedMatrix;
    }
}
