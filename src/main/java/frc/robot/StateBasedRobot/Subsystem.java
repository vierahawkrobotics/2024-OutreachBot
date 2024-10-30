package frc.robot.StateBasedRobot;

public class Subsystem {
    
    private int state = 0;
    public final void SetState(int state) {
        this.state = state;
    }
}