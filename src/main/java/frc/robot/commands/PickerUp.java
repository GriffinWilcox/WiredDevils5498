package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmStuff.PickerUpper;
//Look at you, reading code
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;


public class PickerUp extends Command {
    private PickerUpper armExtension;
	private DoubleSupplier moveArm;
    private DoubleSupplier usePickUp;
    private BooleanSupplier floorPos;
    private BooleanSupplier zeroPos;
    private BooleanSupplier fast;


    public PickerUp(PickerUpper armExtension, DoubleSupplier moveArm, DoubleSupplier usePickUp, BooleanSupplier floorPos, BooleanSupplier zeroPos, BooleanSupplier fast) {
		this.armExtension = armExtension;
        addRequirements(armExtension);

        this.moveArm = moveArm;
        this.usePickUp = usePickUp;
        this.floorPos = floorPos;
        this.zeroPos = zeroPos;
        this.fast = fast;
		
	}

    @Override
	public void initialize() {}

    @Override
	public void execute() {
		armExtension.teleopMove(moveArm.getAsDouble());
        if (fast.getAsBoolean() == true){
            armExtension.pickUp(usePickUp.getAsDouble());
        }
        else if (fast.getAsBoolean() != true){
            armExtension.pickUp(usePickUp.getAsDouble()/4);
        }
        
        

        if (floorPos.getAsBoolean()){
            armExtension.setExtensionPos(500);
        } //This is wild my dude, so crazy
        if (zeroPos.getAsBoolean()){
            armExtension.setExtensionPos(5);
        }
        
        SmartDashboard.putNumber("Pick Up Arm Enc", armExtension.encPos());
        SmartDashboard.putNumber("suckenc", armExtension.suckEnc());
	}

    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
	public void end(boolean interrupted){
		armExtension.setMotorSpeed(0);
	}    
}

