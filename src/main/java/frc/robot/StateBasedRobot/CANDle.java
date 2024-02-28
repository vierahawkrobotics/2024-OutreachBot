package frc.robot.StateBasedRobot;
import com.ctre.phoenix.led.CANdle;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class CANDle extends SubsystemBase {
    private final CANdle cDle;

    public CANDle(){
        cDle = new CANdle(12, "rio");  }

public void setColors(int r, int g, int b){
    cDle.setLEDs(r, g, b);
}
}