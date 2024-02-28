package frc.robot;
import com.ctre.phoenix.led.CANdle;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
public class LedSubsystem extends SubsystemBase {
    private final CANdle cDle;

    public LedSubsystem(){
        cDle = new CANdle(1, "rio");  }

public void setColors(int r, int g, int b){

   cDle.setLEDs(r, g, b);

}
public void off(){
cDle.setLEDs(0, 0, 0);
}
}