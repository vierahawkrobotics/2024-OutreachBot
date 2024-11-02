package frc.robot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Catapult extends SubsystemBase {
    CANSparkMax motor;
    ShuffleboardTab tab;
    State state = State.Retracted;
    private int index=0;
    private GenericEntry controlDegrees;
    private GenericEntry alternateValue;
    private GenericEntry controlPower;
    private Stack<State> queueStates = new Stack<State>();
    private List<Double> angleSet=Arrays.asList(45d,90d);

    public int mod(int n,int m){
        return ((n<0)?((n%m)+m):n%m)%m;
    }
    public Catapult(Robot robot){
        //Set up motor settings
        motor = new CANSparkMax(5, MotorType.kBrushless);
        motor.restoreFactoryDefaults();
        //`motor.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 5);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setInverted(true);
        
        //Setup Shuffleboard
        int len = angleSet.size();
        tab = Shuffleboard.getTab("Catapult Position");
        tab.addDouble("Current Position", () -> {return motor.getEncoder().getPosition();});
        tab.addString("State", ()->{return state.toString();});
        tab.addString("Angle Queue",()->{return String.format("%.1f <- %.1f -> %.1f",angleSet.get(mod(index-1,len)),angleSet.get(index),angleSet.get(mod(index+1,len)));});
        alternateValue=tab.add("Do Alternate Value", false).getEntry();
        controlDegrees=tab.add("Arm Max Angle", 45).getEntry();
        controlPower=tab.add("Arm Power",1.2).getEntry();
        robot.addPeriodic(() -> {periodic(robot);}, 0.005);

        
    }
    private void periodic(Robot robot) {
        //Handle angle control loop
        double target=60.0;
        if(alternateValue.getBoolean(false)){
            angleSet.set(index,controlDegrees.getDouble(angleSet.get(index)));
            target=controlDegrees.getDouble(target);
        }

        //Handle the command queue
        if(!queueStates.isEmpty() && !(state==State.Retracting || state==State.Extending)){
            State nextState = queueStates.pop();
            if(nextState == State.Extending){
                SetExtending();
            } else if (nextState == State.Retracting){
                SetRetracting();
            }
        }

        //Handle extending and retracting
        double pos = motor.getEncoder().getPosition();
        switch(state) {
        case Retracting:
            motor.set(-0.2);
            if(pos < 1) state = State.Retracted;
            break;
        case Extending:
            motor.set(controlPower.getDouble(10));
            if(pos > (Constants.CatapultConstants.ticksPerRotation)*(target/360)) state = State.Extended;
            break;
        case Retracted:
        case Extended:
        default:
            motor.set(0);
            break;
        }

    }

    public void SetExtending() {
        if(state == State.Retracting || state == State.Extending && queueStates.size()<1){ 
            queueStates.add(State.Extending);
            return;
        } else if(state != State.Retracted) return;
        state = State.Extending;
    }
    public void SetRetracting() {
        if(state == State.Retracting || state == State.Extending && queueStates.size()<1){
            queueStates.add(State.Retracting);
            return;
        } else if(state != State.Extended) return;
        state = State.Retracting;
    }

    public void shoot(){
        //index=0;
        //controlDegrees.setDouble(angleSet.get(index));
        SetExtending();
        SetRetracting();
    }
    public void resetEncoder(){
        motor.getEncoder().setPosition(0);
    }
    public void cycleFocus(int direction){
        index=mod(index+direction,angleSet.size());
        controlDegrees.setDouble(angleSet.get(index));
    }
     

    public enum State {
        Retracting,
        Retracted,
        Extending,
        Extended;
    }
}
