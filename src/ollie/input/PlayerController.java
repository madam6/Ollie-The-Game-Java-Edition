package ollie.input;

import ollie.audio.Sound;
import ollie.characters.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Keyboard controls. Held keys are tracked so that releasing one direction while the other
 * is still held keeps Ollie walking, and so key auto-repeat can't burn through dashes.
 */
public class PlayerController extends KeyAdapter {

    private static final float WALK_SPEED = 4;
    private static final float DASH_SPEED = 14;
    private static final float JUMP_SPEED = 9;

    private final Player player;
    private boolean leftHeld;
    private boolean rightHeld;
    private boolean dashing;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isDead()) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                leftHeld = true;
                updateWalking();
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                rightHeld = true;
                updateWalking();
            }
            case KeyEvent.VK_SPACE, KeyEvent.VK_W, KeyEvent.VK_UP -> {
                if (player.tryJump(JUMP_SPEED)) {
                    Sound.JUMP.play();
                }
            }
            case KeyEvent.VK_Q, KeyEvent.VK_SHIFT -> startDash();
            case KeyEvent.VK_E -> {
                if (player.tryFire()) {
                    Sound.FIRE.play();
                }
            }
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (player.isDead()) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                leftHeld = false;
                updateWalking();
            }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                rightHeld = false;
                updateWalking();
            }
            case KeyEvent.VK_Q, KeyEvent.VK_SHIFT -> {
                dashing = false;
                updateWalking();
            }
            default -> {
            }
        }
    }

    private void startDash() {
        if (dashing || !player.useDash()) {
            return;
        }
        dashing = true;
        player.startWalking(player.isFacingRight() ? DASH_SPEED : -DASH_SPEED);
        Sound.DASH.play();
    }

    private void updateWalking() {
        if (dashing) {
            return;
        }
        if (leftHeld == rightHeld) {
            player.stopWalking();
        } else {
            player.startWalking(leftHeld ? -WALK_SPEED : WALK_SPEED);
        }
    }
}
