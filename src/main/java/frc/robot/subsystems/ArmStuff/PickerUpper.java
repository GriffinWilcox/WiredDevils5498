package frc.robot.subsystems.ArmStuff;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkAnalogSensor;

import edu.wpi.first.math.controller.PIDController;
import com.revrobotics.SparkAbsoluteEncoder.Type;
import com.revrobotics.SparkAnalogSensor.Mode;
import com.revrobotics.SparkMaxAlternateEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ArmConstants;

public class PickerUpper extends SubsystemBase implements ArmConstants {

    public CANSparkMax suckyThing;
    public CANSparkMax moveThing;
    public RelativeEncoder moveEncoder;
    public RelativeEncoder suckyEnc;
    public PIDController armPID;
    public boolean isExtensionDone;
    private static final SparkMaxAlternateEncoder.Type kAltEncType = SparkMaxAlternateEncoder.Type.kQuadrature;
    private static final int kCPR = -10;
    

    public PickerUpper(){
        suckyThing = new CANSparkMax(suckyId, MotorType.kBrushless);
        moveThing = new CANSparkMax(suckyMoveId, MotorType.kBrushless);
        moveEncoder = moveThing.getAlternateEncoder(kAltEncType, kCPR);
        suckyEnc = suckyThing.getEncoder();
        //moveEncoder.setPositionConversionFactor(1024);
        //moveEncoder.setZeroOffset(0);
        armPID = new PIDController(aP, aI, aD);
    }

    public double encPos(){
        return moveEncoder.getPosition();
    }
    public double suckEnc(){
        return suckyEnc.getPosition();
    }

    public void pickUp(double s){
        if (s > 0.05){
            suckyThing.set(s/4);
        }
        else if (s < -0.05){
            suckyThing.set(s);
        }
        else {
            suckyThing.set(0);
        }
        
    }

    
    public void teleopMove(double y) {
        if (y > 0.15){
            setMotorSpeed(y);
        }
        else if (y < -0.15){
            setMotorSpeed(y);
        }
        else {
            setMotorSpeed(0);
        }
    }
    
    public void setMotorSpeed(double speed){
        moveThing.set(speed / 3);
    }

    public void setExtensionPos(double setExtension) {
        if (setExtension > pickUpHeightMax) { 
            setExtension = pickUpHeightMax;
        }

        if (encPos() < (setExtension - 150)) {
            setMotorSpeed(-0.7);
            isExtensionDone = false;
        } else if (encPos() > (setExtension - 150) && encPos() < (setExtension - 10)) {
            setMotorSpeed(-0.2);
            isExtensionDone = false;


        } else if (encPos() > (setExtension + 150)) {
            setMotorSpeed(0.7);
            isExtensionDone = false;
        } else if (encPos() < (setExtension + 150) && encPos() > (setExtension + 10)) {
            setMotorSpeed(0.2);
            isExtensionDone = false;
        }
        
        else {
            setMotorSpeed(-0.1);
            isExtensionDone = true;
        }
    }


    
}
