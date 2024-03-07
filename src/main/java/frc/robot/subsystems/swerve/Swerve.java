package frc.robot.subsystems.swerve;

import frc.lib.math.GeometryUtils;
import frc.robot.SwerveConstants;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

import java.text.BreakIterator;

import com.kauailabs.navx.frc.AHRS;


import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class Swerve extends SubsystemBase {


    public SwerveDriveOdometry swerveOdometry;
    public SwerveModule[] mSwerveMods;
    public AHRS gyro = null;
    public static Relay lightFr;
    public static Relay lightFl;
    public static Relay lightBr;
    public static Relay lightBl;



    public Swerve() {
        
        gyro = new AHRS(SPI.Port.kMXP); 
        //gyro.configFactoryDefault();
        lightFr = new Relay(0);
        lightFl = new Relay(1);
        lightBr = new Relay(2);
        lightBl = new Relay(3);
        lightFl.set(Value.kOn);

        
     

        mSwerveMods = new SwerveModule[] {
           
            new SwerveMod(0, SwerveConstants.Swerve.Mod0.constants),
            new SwerveMod(1, SwerveConstants.Swerve.Mod1.constants),
            new SwerveMod(2, SwerveConstants.Swerve.Mod2.constants),
            new SwerveMod(3, SwerveConstants.Swerve.Mod3.constants)
        };

        swerveOdometry = new SwerveDriveOdometry(SwerveConfig.swerveKinematics, getYaw(), getModulePositions());
        zeroGyro();
        /*Good Job =D */
    }
    private static ChassisSpeeds correctForDynamics(ChassisSpeeds originalSpeeds) {
        final double LOOP_TIME_S = 0.02;
        Pose2d futureRobotPose =
            new Pose2d(
                originalSpeeds.vxMetersPerSecond * LOOP_TIME_S,
                originalSpeeds.vyMetersPerSecond * LOOP_TIME_S,
                Rotation2d.fromRadians(originalSpeeds.omegaRadiansPerSecond * LOOP_TIME_S));
        Twist2d twistForPose = GeometryUtils.log(futureRobotPose);
        ChassisSpeeds updatedSpeeds =
            new ChassisSpeeds(
                twistForPose.dx / LOOP_TIME_S,
                twistForPose.dy / LOOP_TIME_S,
                twistForPose.dtheta / LOOP_TIME_S);
        return updatedSpeeds;
    }


    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop) {
        ChassisSpeeds desiredChassisSpeeds =
        fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
        translation.getX(),
        translation.getY(),
        rotation,
        getYaw())
        : new ChassisSpeeds(
                translation.getX(),
                translation.getY(),
                rotation);
        desiredChassisSpeeds = correctForDynamics(desiredChassisSpeeds);

        SwerveModuleState[] swerveModuleStates = SwerveConfig.swerveKinematics.toSwerveModuleStates(desiredChassisSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveConfig.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(swerveModuleStates[mod.getModuleNumber()], isOpenLoop);
        }

    }    
    /* Used by SwerveControllerCommand in Auto */
    public void setModuleStates(SwerveModuleState[] desiredStates) {

       // System.out.println("setting module states: "+desiredStates[0]);
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, SwerveConfig.maxSpeed);
        
        for(SwerveModule mod : mSwerveMods){
            mod.setDesiredState(desiredStates[mod.getModuleNumber()], false);
        }
    }    
    public Pose2d getPose() {
        Pose2d p =  swerveOdometry.getPoseMeters();
        return new Pose2d(-p.getX(),-p.getY(),  p.getRotation());
    }
    public void resetOdometry(Pose2d pose) {
        
        swerveOdometry.resetPosition(new Rotation2d(), getModulePositions(), pose);
        zeroGyro(pose.getRotation().getDegrees());
       
    }
    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for(SwerveModule mod : mSwerveMods) {
            states[mod.getModuleNumber()] = mod.getState();
        }
        return states;
    }

    public SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for(SwerveModule mod : mSwerveMods) {
            positions[mod.getModuleNumber()] = mod.getPosition();
        }
        return positions;
    }

    public void zeroGyro(double deg) {
        if(SwerveConfig.invertGyro) {
            deg = -deg;
        }
        gyro.reset();
        swerveOdometry.update(getYaw(), getModulePositions());  
    }

    public void zeroGyro() {  
       zeroGyro(0);
    }

    public Rotation2d getYaw() {
        return (SwerveConfig.invertGyro) ? Rotation2d.fromDegrees(360 - gyro.getYaw()) : Rotation2d.fromDegrees(gyro.getYaw());
    }

    public void autonDrive(double s){
        if (mSwerveMods[0].getModuleNumber() == 0){
            ((SwerveMod) mSwerveMods[0]).setSpeed2(s);
        }
        if (mSwerveMods[1].getModuleNumber() == 1){
            ((SwerveMod) mSwerveMods[1]).setSpeed2(s*-1);
        }
        if (mSwerveMods[2].getModuleNumber() == 2){
            ((SwerveMod) mSwerveMods[2]).setSpeed2(s);
        }
        if (mSwerveMods[3].getModuleNumber() == 3){
            ((SwerveMod) mSwerveMods[3]).setSpeed2(s*-1);
        }

    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("yaw", gyro.getYaw());
        for(SwerveModule mod : mSwerveMods) {
            SmartDashboard.putNumber("REV Mod " + mod.getModuleNumber() + " Cancoder", mod.getCanCoder().getDegrees());
            SmartDashboard.putNumber("REV Mod " + mod.getModuleNumber() + " Integrated", mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("REV Mod " + mod.getModuleNumber() + " Velocity", mod.getState().speedMetersPerSecond);   
            //
        }
        
        if (mSwerveMods[0].getCanCoder().getDegrees() >= 286.1 && mSwerveMods[0].getCanCoder().getDegrees() <= 290.1){//set to actual cancoder zero point
            lightFl.set(Value.kOn);
        }   
        else {
            lightFl.set(Value.kOff);
        }
            //
            
        if (mSwerveMods[1].getCanCoder().getDegrees() >= 39.6 && mSwerveMods[1].getCanCoder().getDegrees() <= 43.6){//set to actual cancoder zero point
            lightFr.set(Value.kOn);
        }
        else {
            lightFr.set(Value.kOff);
        }
            //
        
        if (mSwerveMods[2].getCanCoder().getDegrees() >= 94.5 && mSwerveMods[2].getCanCoder().getDegrees() <= 98.5){//set to actual cancoder zero point
            lightBl.set(Value.kOn);
        }
        else {
            lightBl.set(Value.kOff);
        }
            //
            
        if (mSwerveMods[3].getCanCoder().getDegrees() >= 119.9 && mSwerveMods[3].getCanCoder().getDegrees() <= 123.9){//set to actual cancoder zero point
            lightBr.set(Value.kOn);
        }
        else {
            lightBr.set(Value.kOff);
        }
    }
}