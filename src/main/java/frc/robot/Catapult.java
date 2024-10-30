//Commit Changes
package frc.robot;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Catapult extends SubsystemBase {
    CANSparkMax motor;
    ShuffleboardTab tab;
    State state = State.Retracted;

    public Catapult(Robot robot){
        motor = new CANSparkMax(5, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        //`motor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 5);
        motor.setIdleMode(IdleMode.kBrake);
        tab = Shuffleboard.getTab("Catapult Position");
        motor.setInverted(true);
        tab.addDouble("Current Position", () -> {return motor.getEncoder().getPosition();});
        robot.addPeriodic(() -> {periodic(robot);}, 0.005);
    }
    private void periodic(Robot robot) {
        double pos = motor.getEncoder().getPosition();
        switch(state) {
        case Retracting:
            motor.set(-0.2);
            if(pos < 1) state = State.Retracted;
            break;
        case Extending:
            motor.set(10);
            if(pos > 7) state = State.Extended;
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
     

    public enum State {
        Retracting,
        Retracted,
        Extending,
        Extended;
    }
}
