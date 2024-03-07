package frc.robot.subsystems.ArmStuff;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ArmConstants;

public class Climber extends SubsystemBase implements ArmConstants{
    public CANSparkMax climbMotor1;
    public CANSparkMax climbMotor2;

    public Climber(){
        climbMotor1 = new CANSparkMax(climbId1, MotorType.kBrushless);
        climbMotor2 = new CANSparkMax(climbId2, MotorType.kBrushless);
    }

    public void climbUp(double s){
        climbMotor1.set(s);
        climbMotor2.set(s);
    }

    public void climbD1(double s){
        climbMotor1.set(s);
    }

    public void climbD2(double s){
        climbMotor2.set(s);
    }
}
