package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Catapult extends SubsystemBase {
    CANSparkMax motor;
    ShuffleboardTab tab;
    State state = State.Retracted;
    final private double HAMBURGER_SHOOT_MAX = 45;
    private double curLaunchAngle = 0;
    XboxController controller1 = new XboxController(1);

    public Catapult(Robot robot){
        motor = new CANSparkMax(5, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        motor.setIdleMode(IdleMode.kBrake);
        tab = Shuffleboard.getTab("Catapult Position");
        motor.setInverted(true);
        tab.addDouble("Current Position", () -> {return motor.getEncoder().getPosition();});
        robot.addPeriodic(() -> {periodic(robot);}, 0.005);
    }
    private void periodic(Robot robot) {
        double pos = motor.getEncoder().getPosition();
        setArmPosition();
        switch(state) {
        case Retracting:
            motor.set(-0.2);
            if(pos < 1) state = State.Retracted;
            break;
        case Extending:
            motor.set(.2);
            if(pos > curLaunchAngle) state = State.Extended;
            break;
        case Retracted:
        case Extended:
        default:
            motor.set(0);
            break;
        }
    }
    public void SetExtending() {
        if(state != State.Retracted) return;
        state = State.Extending;
    }
    public void SetRetracting() {
        if(state != State.Extended) return;
        state = State.Retracting;
    }
    public void setArmPosition() {
        if (curLaunchAngle <= HAMBURGER_SHOOT_MAX && curLaunchAngle >= 0) {
            if (controller1.getAButton()) {
                curLaunchAngle += .1;
            } else if (controller1.getYButton()) {
                curLaunchAngle -= .1;
            }
            tab.addDouble("Launch Angle", () -> curLaunchAngle);
        }
    }
    public enum State {
        Retracting,
        Retracted,
        Extending,
        Extended;
    }
}