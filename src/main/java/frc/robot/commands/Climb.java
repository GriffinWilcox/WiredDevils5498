/*package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmStuff.Climber;

public class Climb extends Command {
    private Climber climb;
    private BooleanSupplier up;
    private DoubleSupplier down1;
    private DoubleSupplier down2;

    public Climb(Climber climb, BooleanSupplier up, DoubleSupplier down1, DoubleSupplier down2){
        this.climb = climb;
        addRequirements(climb);
        this.up = up;
        this.down1 = down1;
        this.down2 = down2;
    }

    @Override
    public void execute(){
        if (up.getAsBoolean()){
            climb.climbUp(0.4);
        }//Sup Boi
        else if(down1.getAsDouble() < 0.1 && down2.getAsDouble() < 0.1) {
            climb.climbUp(0);
        }

        if (down1.getAsDouble() > 0.1){
            climb.climbD1(down1.getAsDouble() *-1);
        }
        else if(up.getAsBoolean() != true){
            climb.climbD1(0);
        }
        

        if (down2.getAsDouble() > 0.1){
            climb.climbD2(down2.getAsDouble() *-1);
        }
        else if(up.getAsBoolean() != true){
            climb.climbD2(0);
        }


    }

    
}
*/