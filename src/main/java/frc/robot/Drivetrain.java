package frc.robot;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Drivetrain {
    private TalonSRX leftLeader;
    private TalonSRX leftFollower;
    private TalonSRX rightLeader;
    private TalonSRX rightFollower;
    public static double maxSpeed;
    private double maxTurnSpeed;
    private double deadzone;
    private PIDController pidLeft;
    private PIDController pidRight;
    private DifferentialDriveKinematics kinematics;
    ShuffleboardTab currentSpeed;
    private double kTicksPerDegree = 4096./360;
    
    public Drivetrain() {
        leftLeader = new TalonSRX(4);
        leftFollower = new TalonSRX(3);
        rightLeader = new TalonSRX(1);
        rightFollower = new TalonSRX(2);

        leftLeader.configFactoryDefault();
        rightLeader.configFactoryDefault();
        rightFollower.follow(rightLeader);  
        leftFollower.follow(leftLeader);
        leftLeader.setInverted(true);
        rightLeader.setInverted(false);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightFollower.setInverted(InvertType.FollowMaster);

        maxTurnSpeed = -1.3;
        deadzone = 0.25;

        pidLeft = new PIDController(.4, 0,0);
        pidRight = new PIDController(0.4, 0,0);

        kinematics = new DifferentialDriveKinematics(0.65);
        currentSpeed = Shuffleboard.getTab("Drivetrain Speed");
        currentSpeed.addDouble("Current Left Speed", () -> {return getLeftSpeed();});
        currentSpeed.addDouble("Current Right Speed", () -> {return getRightSpeed();});
    }

    public void Set(double forward, double side) {
        side = clamp(deadZone(side,deadzone),1) * maxTurnSpeed;
        forward = -clamp(deadZone(forward,deadzone),1) * maxSpeed;

        DifferentialDriveWheelSpeeds s = kinematics.toWheelSpeeds(new ChassisSpeeds(forward,0,side));
        
        leftLeader.set(TalonSRXControlMode.PercentOutput,pidLeft.calculate(getLeftSpeed(), s.leftMetersPerSecond));
        rightLeader.set(TalonSRXControlMode.PercentOutput,pidRight.calculate(getRightSpeed(), s.rightMetersPerSecond));
    }

    private double getLeftSpeed() {
        return leftLeader.getSelectedSensorVelocity() * kTicksPerDegree * 0.0762;
    }
    private double getRightSpeed() {
        return rightLeader.getSelectedSensorVelocity() * kTicksPerDegree * 0.0762;
    }

    private double clamp(double v, double mag) {
        return (v > mag ? mag: (v < -mag ? -mag: v));
    }
    private double deadZone(double v, double mag) {
        v*= 1+mag;
        return (v > mag ? v-mag: (v < -mag ? v+mag: 0));
    }
}
//100% speed forward for set time (increment value?). reverse slow with limit switch. states