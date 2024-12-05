
package frc.robot.subsystems.swerve;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import frc.lib.util.swerveUtil.CTREModuleState;
import frc.lib.util.swerveUtil.RevSwerveModuleConstants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.FaultID;
import com.revrobotics.CANSparkLowLevel.MotorType;

/**
 * a Swerve Modules using REV Robotics motor controllers and CTRE CANcoder absolute encoders.
 */
public class SwerveMod implements SwerveModule
{
    public int moduleNumber;
    private Rotation2d angleOffset;
   // private Rotation2d lastAngle;

    private CANSparkMax mAngleMotor;
    private CANSparkMax mDriveMotor;




    private CANcoder angleEncoder;
    private RelativeEncoder relAngleEncoder;
    private RelativeEncoder relDriveEncoder;


    //SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(Constants.Swerve.driveKS, Constants.Swerve.driveKV, Constants.Swerve.driveKA);

    public SwerveMod(int moduleNumber, RevSwerveModuleConstants moduleConstants)
    {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;
        
       
        /* Angle Motor Config */
        mAngleMotor = new CANSparkMax(moduleConstants.angleMotorID, MotorType.kBrushless);
        configAngleMotor();
        //mAngleMotor.setInverted(true);
        /* Drive Motor Config */
        mDriveMotor = new CANSparkMax(moduleConstants.driveMotorID,  MotorType.kBrushless);
        configDriveMotor();

         /* Angle Encoder Config */
    
        angleEncoder = new CANcoder(moduleConstants.cancoderID);
        configEncoders();


       // lastAngle = getState().angle;
    }


    private void configEncoders()
    {     
        // absolute encoder   
      
        angleEncoder.getConfigurator().apply(new CANcoderConfiguration());
        angleEncoder.getConfigurator().apply(new SwerveConfig().canCoderConfig);
       
        relDriveEncoder = mDriveMotor.getEncoder();
        relDriveEncoder.setPosition(0);

         
        relDriveEncoder.setPositionConversionFactor(SwerveConfig.driveRevToMeters);
        relDriveEncoder.setVelocityConversionFactor(SwerveConfig.driveRpmToMetersPerSecond);

        
        relAngleEncoder = mAngleMotor.getEncoder();
        relAngleEncoder.setPositionConversionFactor(SwerveConfig.DegreesPerTurnRotation);
        // in degrees/sec
        relAngleEncoder.setVelocityConversionFactor(SwerveConfig.DegreesPerTurnRotation / 60);
    

        resetToAbsolute();
        mDriveMotor.burnFlash();
        mAngleMotor.burnFlash();
        
    }

    private void configAngleMotor()
    {
        mAngleMotor.restoreFactoryDefaults();
        SparkPIDController controller = mAngleMotor.getPIDController();
        controller.setP(SwerveConfig.angleKP, 0);
        controller.setI(SwerveConfig.angleKI,0);
        controller.setD(SwerveConfig.angleKD,0);
        controller.setFF(SwerveConfig.angleKF,0);
        controller.setOutputRange(-SwerveConfig.anglePower, SwerveConfig.anglePower);
        mAngleMotor.setSmartCurrentLimit(SwerveConfig.angleContinuousCurrentLimit);
       
        mAngleMotor.setInverted(SwerveConfig.angleMotorInvert);
        mAngleMotor.setIdleMode(SwerveConfig.angleIdleMode);

        
       
    }

    private void configDriveMotor()
    {        
        mDriveMotor.restoreFactoryDefaults();
        SparkPIDController controller = mDriveMotor.getPIDController();
        controller.setP(SwerveConfig.driveKP,0);
        controller.setI(SwerveConfig.driveKI,0);
        controller.setD(SwerveConfig.driveKD,0);
        controller.setFF(SwerveConfig.driveKF,0);
        controller.setOutputRange(-SwerveConfig.drivePower, SwerveConfig.drivePower);
        mDriveMotor.setSmartCurrentLimit(SwerveConfig.driveContinuousCurrentLimit);
        mDriveMotor.setInverted(SwerveConfig.driveMotorInvert);
        mDriveMotor.setIdleMode(SwerveConfig.driveIdleMode); 
    
       
       
       
    }



    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop)
    {
        
        
        // CTREModuleState functions for any motor type.
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);

        if(mDriveMotor.getFault(FaultID.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Drive Motor ID:"+mDriveMotor.getDeviceId(), false);
        }

        if(mAngleMotor.getFault(FaultID.kSensorFault))
        {
            DriverStation.reportWarning("Sensor Fault on Angle Motor ID:"+mAngleMotor.getDeviceId(), false);
        }
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop)
    {
       
        if(isOpenLoop)
        {
            double percentOutput = desiredState.speedMetersPerSecond / SwerveConfig.maxSpeed;
            mDriveMotor.set(percentOutput);
            return;
        }
 
        double velocity = desiredState.speedMetersPerSecond;
        
        SparkPIDController controller = mDriveMotor.getPIDController();
        controller.setReference(velocity, ControlType.kVelocity, 0);
     // Keep It Up :)   
    }
    
    public void setSpeed2(double s){
        
        /////////////// mod 0 ////////////////////
        if (moduleNumber == 0){
        double targetMin0 = 148;
        double targetMax0 = 154;
        double targetException0 = 151;

        while (getCanCoder().getDegrees() < targetMin0 || (getCanCoder().getDegrees() > targetMax0 && getCanCoder().getDegrees() != targetException0)) {
            double currentAngle0 = getCanCoder().getDegrees();
            double distanceToTargetMin0 = Math.abs(currentAngle0 - targetMin0);
            double distanceToTargetMax0 = Math.abs(currentAngle0 - targetMax0);
    
            double distanceToTarget0 = Math.min(distanceToTargetMin0, distanceToTargetMax0);
    
            double motorSpeed = 0.1; // Default speed
    
            if (distanceToTarget0 < 5) {
                motorSpeed = 0.025; 
            } else if (distanceToTarget0 < 10) {
                motorSpeed = 0.035; 
            }
            mAngleMotor.set(motorSpeed);
        }
        while (getCanCoder().getDegrees() < 150 || getCanCoder().getDegrees() > 152) {
            double currentAngle0 = getCanCoder().getDegrees();
    
            double distanceToTarget0 = Math.abs(currentAngle0 - 151); 
    
            double motorSpeed = 0.025;
            if (distanceToTarget0 < 1) {
                motorSpeed = 0.01;
            } else if (distanceToTarget0 < 2) {
                motorSpeed = 0.02; 
            }
            mAngleMotor.set(motorSpeed);
        }
        if (getCanCoder().getDegrees() >= 150 && getCanCoder().getDegrees() <= 152) {
            mAngleMotor.set(0); // Stop motor
            relAngleEncoder.setPosition(0); // Reset encoder position
        }
        }
        //////////////////////////////////

        /////////////// mod 1 ////////////////////
        if (moduleNumber == 1){
        double targetMin1 = 38;
        double targetMax1 = 44;
        double targetException1 = 41;

        while (getCanCoder().getDegrees() < targetMin1 || (getCanCoder().getDegrees() > targetMax1 && getCanCoder().getDegrees() != targetException1)) {
            double currentAngle1 = getCanCoder().getDegrees();
            double distanceToTargetMin1 = Math.abs(currentAngle1 - targetMin1);
            double distanceToTargetMax1 = Math.abs(currentAngle1 - targetMax1);
    
            double distanceToTarget1 = Math.min(distanceToTargetMin1, distanceToTargetMax1);
    
            double motorSpeed = 0.1; // Default speed
    
            if (distanceToTarget1 < 5) {
                motorSpeed = 0.025; 
            } else if (distanceToTarget1 < 10) {
                motorSpeed = 0.035; 
            }
            mAngleMotor.set(motorSpeed);
        }
        while (getCanCoder().getDegrees() < 40 || getCanCoder().getDegrees() > 42) {
            double currentAngle1 = getCanCoder().getDegrees();
    
            double distanceToTarget1 = Math.abs(currentAngle1 - 41); 
    
            double motorSpeed = 0.025;
            if (distanceToTarget1 < 1) {
                motorSpeed = 0.01;
            } else if (distanceToTarget1 < 2) {
                motorSpeed = 0.02; 
            }
            mAngleMotor.set(motorSpeed);
        }
        if (getCanCoder().getDegrees() >= 40 && getCanCoder().getDegrees() <= 42) {
            mAngleMotor.set(0); // Stop motor
            relAngleEncoder.setPosition(0); // Reset encoder position
        }
        }
        //////////////////////////////////

        /////////////// mod 2 ////////////////////
        if (moduleNumber == 2){
        double targetMin2 = 93;
        double targetMax2 = 99;
        double targetException2 = 96;

        while (getCanCoder().getDegrees() < targetMin2 || (getCanCoder().getDegrees() > targetMax2 && getCanCoder().getDegrees() != targetException2)) {
            double currentAngle2 = getCanCoder().getDegrees();
            double distanceToTargetMin2 = Math.abs(currentAngle2 - targetMin2);
            double distanceToTargetMax2 = Math.abs(currentAngle2 - targetMax2);
    
            double distanceToTarget2 = Math.min(distanceToTargetMin2, distanceToTargetMax2);
    
            double motorSpeed = 0.1; // Default speed
    
            if (distanceToTarget2 < 5) {
                motorSpeed = 0.025; 
            } else if (distanceToTarget2 < 10) {
                motorSpeed = 0.035; 
            }
            mAngleMotor.set(motorSpeed);
        }
        while (getCanCoder().getDegrees() < 95 || getCanCoder().getDegrees() > 97) {
            double currentAngle2 = getCanCoder().getDegrees();
    
            double distanceToTarget2 = Math.abs(currentAngle2 - 96); 
    
            double motorSpeed = 0.025;
            if (distanceToTarget2 < 1) {
                motorSpeed = 0.01;
            } else if (distanceToTarget2 < 2) {
                motorSpeed = 0.02; 
            }
            mAngleMotor.set(motorSpeed);
        }
        if (getCanCoder().getDegrees() >= 95 && getCanCoder().getDegrees() <= 97) {
            mAngleMotor.set(0); // Stop motor
            relAngleEncoder.setPosition(0); // Reset encoder position
        }
        }
        //////////////////////////////////

        /////////////// mod 3 ////////////////////
        if (moduleNumber == 3){
        double targetMin3 = 118;
        double targetMax3 = 124;
        double targetException3 = 121;

        while (getCanCoder().getDegrees() < targetMin3 || (getCanCoder().getDegrees() > targetMax3 && getCanCoder().getDegrees() != targetException3)) {
            double currentAngle3 = getCanCoder().getDegrees();
            double distanceToTargetMin3 = Math.abs(currentAngle3 - targetMin3);
            double distanceToTargetMax3 = Math.abs(currentAngle3 - targetMax3);
    
            double distanceToTarget3 = Math.min(distanceToTargetMin3, distanceToTargetMax3);
    
            double motorSpeed = 0.1; // Default speed
    
            if (distanceToTarget3 < 5) {
                motorSpeed = 0.025; 
            } else if (distanceToTarget3 < 10) {
                motorSpeed = 0.035; 
            }
            mAngleMotor.set(motorSpeed);
        }
        while (getCanCoder().getDegrees() < 120 || getCanCoder().getDegrees() > 122) {
            double currentAngle3 = getCanCoder().getDegrees();
    
            double distanceToTarget3 = Math.abs(currentAngle3 - 121); 
    
            double motorSpeed = 0.025;
            if (distanceToTarget3 < 1) {
                motorSpeed = 0.01;
            } else if (distanceToTarget3 < 2) {
                motorSpeed = 0.02; 
            }
            mAngleMotor.set(motorSpeed);
        }
        if (getCanCoder().getDegrees() >= 120 && getCanCoder().getDegrees() <= 122) {
            mAngleMotor.set(0); // Stop motor
            relAngleEncoder.setPosition(0); // Reset encoder position
        }
        }
        //////////////////////////////////

    }
    
    private void setAngle(SwerveModuleState desiredState)
    {
        if(Math.abs(desiredState.speedMetersPerSecond) <= (SwerveConfig.maxSpeed * 0.01)) 
        {
            mAngleMotor.stopMotor();
            return;

        }
        Rotation2d angle = desiredState.angle; 
        //Prevent rotating module if speed is less then 1%. Prevents Jittering.
        
        SparkPIDController controller = mAngleMotor.getPIDController();
        
        double degReference = angle.getDegrees();
     
       
        
        controller.setReference (degReference, ControlType.kPosition, 0);
        
    }

   

    private Rotation2d getAngle()
    {
        return Rotation2d.fromDegrees(relAngleEncoder.getPosition());
    }

    public Rotation2d getCanCoder()
    {
        
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition().getValue()*360); //*360 
        //return getAngle();
    }

    public int getModuleNumber() 
    {
        return moduleNumber;
    }

    public void setModuleNumber(int moduleNumber) 
    {
        this.moduleNumber = moduleNumber;
    }

    private void resetToAbsolute()
    {
    
        double absolutePosition = getCanCoder().getDegrees() - angleOffset.getDegrees();
        relAngleEncoder.setPosition(absolutePosition);
    }

  

    public SwerveModuleState getState()
    {
        return new SwerveModuleState(
            relDriveEncoder.getVelocity(),
            getAngle()
        ); 
    }

    public SwerveModulePosition getPosition()
    {
        return new SwerveModulePosition(
            relDriveEncoder.getPosition(), 
            getAngle()
        );
    }
}