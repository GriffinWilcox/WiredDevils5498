package frc.robot.subsystems.ArmStuff;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import frc.robot.ArmConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Shooter extends SubsystemBase implements ArmConstants  {

    public CANSparkMax ringShooter1;
    public CANSparkMax ringShooter2;
    

    public Shooter(){
        ringShooter1 = new CANSparkMax(shooterId1, MotorType.kBrushless);
        ringShooter2 = new CANSparkMax(shooterId2, MotorType.kBrushless);
    }

    public void setSpeed(double s){
        ringShooter1.set(s);
        ringShooter2.set(s * -1);
    }
    /* 
    public void getVelo(){
        
    }
    */


    
}
