package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swerve.Swerve;
import edu.wpi.first.wpilibj.Timer;

public class AutonRun extends Command {

    private Swerve swerve;
    Timer timer = new Timer();
    boolean isDriveFinished = false;

    public AutonRun(Swerve swerve){
        this.swerve = swerve;
        timer.reset();
    }

    @Override
    public void execute(){
        timer.start();
        swerve.autonDrive(0.5);
    }
    
    @Override
    public boolean isFinished() {
        if(timer.get() > 1){
            isDriveFinished = true;
        }
        else {
            isDriveFinished = false;
        }
        return isDriveFinished;
    }

    @Override
    public void end(boolean interrupted){
        swerve.autonDrive(0);
        timer.stop();
    }
}
