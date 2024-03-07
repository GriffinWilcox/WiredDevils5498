package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmStuff.Actuator;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class ActuatorRun extends Command {
    private Actuator actuator;
    private DoubleSupplier move; 
    private BooleanSupplier zero;
    private BooleanSupplier shoot;

    public ActuatorRun(Actuator actuator, DoubleSupplier move, BooleanSupplier zero, BooleanSupplier shoot){
        this.actuator = actuator;
        addRequirements(actuator);
        this.move = move;
        this.zero = zero;
        this.shoot = shoot;
    }

    @Override
    public void execute(){
        // all zeros are placeholders until i get actual enc values  to go off 
        if (actuator.getEnc() >= -17.285681 && actuator.getEnc() < 6.095235){
            if (move.getAsDouble() > 0.20 || move.getAsDouble() < -0.20){
                actuator.moveActuator(move.getAsDouble()/3);
            }
            else {
                actuator.moveActuator(0);
            }
        }
        else if(actuator.getEnc() < -17.285681 && move.getAsDouble() > 0.20) {
            actuator.moveActuator(move.getAsDouble()/3);
        }    
        else if(actuator.getEnc() > 6.095235 && move.getAsDouble() < -0.20) {
            actuator.moveActuator(move.getAsDouble()/3);
        }
        else {
            actuator.moveActuator(0);
        }
        SmartDashboard.putNumber("actEnc", actuator.getEnc());
        /////////////////////////////////Got enough slashes??////////////////////////////////////// no
        if (zero.getAsBoolean()){
            if (actuator.getEnc() > 0.2){
                actuator.moveActuator(-0.2);
            }
            else if (actuator.getEnc() < -0.2){
                actuator.moveActuator(0.2);
            }
            else {
                actuator.moveActuator(0);
            }
        }

        if (shoot.getAsBoolean()){
            if (actuator.getEnc() > -13.0){ // 0 is a placeholder
                actuator.moveActuator(-0.05);
            }
            else if (actuator.getEnc() > -12.8){
                actuator.moveActuator(-0.02);
            }
            else if (actuator.getEnc() < -12.4){ // 0 is a placeholder
                actuator.moveActuator(0.05);
            }
            else if (actuator.getEnc() < -12.6){
                actuator.moveActuator(0.02);
            }
            else {
                actuator.moveActuator(0);
            }
        }
    }

    
    
}
