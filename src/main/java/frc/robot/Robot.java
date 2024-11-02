// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Catapult;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 * 
 * adfjasd;lfjasd;lfjk
 */
public class Robot extends TimedRobot {
  public Joystick controller;
  public Drivetrain drivetrain;
  public Catapult catapult;
  private int shift;
  //public LedSubsystem ledsubsystem;
  /**
   *
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
      controller = new Joystick(1);
      drivetrain = new Drivetrain();
      catapult = new Catapult(this);
      Shuffleboard.getTab("Catapult Position").addDouble("Direction",()->{return controller.getPOV();});
  }

  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    drivetrain.Set(-controller.getY(), controller.getZ());
    int dir = controller.getPOV();
    if(controller.getRawButtonPressed(4)){
      catapult.SetExtending();
    }
    if(controller.getRawButtonPressed(2)){
      catapult.SetRetracting();
    }
    if(controller.getRawButtonPressed(1)) {
      catapult.shoot();
    }
    if(controller.getRawButton(10)){
      catapult.resetEncoder();
    }
    if(dir == 270 && shift !=-1){
      shift=-1;
      catapult.cycleFocus(-1);
    } else if(dir == 90 && shift!=1){
      shift=1;
      catapult.cycleFocus(1);
    }
    
    /*   cs.checkIRandProximity();
      cs.putDatainSmartDashboard();
      
      if(controller.getRawButtonPressed(1)){
        cs.setMotorMotion();
    
        
      }
      if(controller.getRawButtonReleased(1)){
        cs.stopMotor();
        
      }
      if(controller.getRawButtonPressed(7)){
        cs.setMotorMotion();
        
      }
      
     */
      
    }

  @Override
  public void disabledInit() {
      catapult.SetRetracting();
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
