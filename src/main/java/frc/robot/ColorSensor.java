package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.CANSparkMax;
public class ColorSensor extends SubsystemBase {

    private final I2C.Port sensorport = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorsensor;
    public CANSparkMax testmotor;
    double motorspeed = 10;

    public ColorSensor(){
    m_colorsensor = new ColorSensorV3(sensorport);
    testmotor = new CANSparkMax(6, MotorType.kBrushed);
    testmotor.setIdleMode(IdleMode.kBrake);

    }
  
    public double getInfraredLightValue(){
        double irvalue = m_colorsensor.getIR();
        return irvalue;
    }
    public int getProximity(){
        int prox = m_colorsensor.getProximity();
        return prox;
    }
    public void putDatainSmartDashboard(){
        Color detectedcolor = m_colorsensor.getColor();
        SmartDashboard.putNumber("Red", detectedcolor.red * 255);
        SmartDashboard.putNumber("Blue", detectedcolor.blue*255);
        SmartDashboard.putNumber("Green", detectedcolor.green*255);
        SmartDashboard.putNumber("IR", getInfraredLightValue());
        SmartDashboard.putNumber("Proximity", getProximity());
        SmartDashboard.putNumber("Speed", testmotor.get());
    }
    public void setMotorMotion(){
        testmotor.set(motorspeed * 0.999);
        

    }
    public void checkIRandProximity(){
        
        if(getProximity() > 250){
            testmotor.set(motorspeed * 0.01);
        }
    }
    public void stopMotor(){
        testmotor.set(0);
    }
    public void disableSensor(){

    }
    
}
