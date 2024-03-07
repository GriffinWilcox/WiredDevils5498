package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmStuff.Shooter;

public class ShootRing extends Command{
    private Shooter shooter;
    private DoubleSupplier shoot;
    private BooleanSupplier fast;

    public ShootRing(Shooter shooter, DoubleSupplier shoot, BooleanSupplier fast){
        this.shooter = shooter;
        addRequirements(shooter);
        this.shoot = shoot;
        this.fast = fast;

    }

    @Override
    public void execute(){

        if (shoot.getAsDouble() > 0.15 && fast.getAsBoolean() == false){
            shooter.setSpeed(0.2);
        }
        else if (shoot.getAsDouble() > 0.15 && fast.getAsBoolean() == true){
            shooter.setSpeed(0.5);
        }
        else {
            shooter.setSpeed(0);
        }




    }


}
