package frc.robot;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;


import frc.robot.subsystems.swerve.SwerveConfig;

public final class CTREConfigs {
  
    public CANcoderConfiguration swerveCanCoderConfig;

    public CTREConfigs(){
     
        swerveCanCoderConfig = new CANcoderConfiguration();

        
        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
        swerveCanCoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        //swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        //swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}