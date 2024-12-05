/*package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.swerve.Swerve;
import java.util.function.BooleanSupplier;

public class ZeroWheels extends Command {

    private Swerve swerve;
    private BooleanSupplier zero;

    public ZeroWheels(Swerve swerve, BooleanSupplier zero){
        this.swerve = swerve;
        this.zero = zero;
        addRequirements(swerve);
    }

    @Override
    public void execute(){
        if(zero.getAsBoolean() == true){
            swerve.setSpeed3();
        }
    }
}
*/