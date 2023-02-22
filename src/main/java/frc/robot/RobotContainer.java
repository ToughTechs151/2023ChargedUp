// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIconstants;
import frc.robot.commands.ArmDownCommand;
import frc.robot.commands.ArmExtendCommand;
import frc.robot.commands.ArmRetractCommand;
import frc.robot.commands.ArmUpCommand;
import frc.robot.commands.ClawCloseCommand;
import frc.robot.commands.ClawOpenCommand;
import frc.robot.subsystems.ArmPidSubsystem;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private PowerDistribution pdp = new PowerDistribution();

  // The robot's subsystems
  private final DriveSubsystem robotDrive = new DriveSubsystem();

  private final ClawSubsystem clawSubsystem = new ClawSubsystem();

  private final ArmPidSubsystem armSubsystem = new ArmPidSubsystem();

  private final ArmSubsystem armSystem = new ArmSubsystem();

  // The driver's controller
  XboxController driverController = new XboxController(OIconstants.DRIVER_CONTROLLER_PORT);

  private CommandXboxController codriverController =
      new CommandXboxController(Constants.OIconstants.CODRIVER_CONTROLLER_PORT);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    this.robotDrive.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new RunCommand(
            () ->
                this.robotDrive.drive(
                    -this.driverController.getLeftY(),
                    this.driverController.getLeftX(),
                    this.driverController.getRightX(),
                    false),
            this.robotDrive));
    SmartDashboard.putData("ArmSubsystem", armSubsystem);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    // Drive at half speed when the right bumper is held
    new JoystickButton(this.driverController, Button.kRightBumper.value)
        .onTrue(new InstantCommand(() -> this.robotDrive.setMaxOutput(0.5)))
        .onFalse(new InstantCommand(() -> this.robotDrive.setMaxOutput(1)));
    codriverController.leftBumper().onTrue(new ClawOpenCommand(clawSubsystem));
    codriverController.rightBumper().onTrue(new ClawCloseCommand(clawSubsystem));
        codriverController.leftTrigger().onTrue(new ArmExtendCommand(armSystem));
        codriverController.rightTrigger().onTrue(new ArmRetractCommand(armSystem));


    new JoystickButton(this.driverController, Button.kA.value)

    new JoystickButton(this.driverController, Button.kB.value)
        .onTrue(new ArmDownCommand(armSubsystem));

        

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    /* Return null if there is no autonomous command to run. */
    return null;
    // // Create config for trajectory
    // TrajectoryConfig config =
    //     new TrajectoryConfig(
    //             AutoConstants.kMaxSpeedMetersPerSecond,
    //             AutoConstants.kMaxAccelerationMetersPerSecondSquared)
    //         // Add kinematics to ensure max speed is actually obeyed
    //         .setKinematics(DriveConstants.kDriveKinematics);
    // // An example trajectory to follow.  All units in meters.
    // Trajectory exampleTrajectory =
    //     TrajectoryGenerator.generateTrajectory(
    //         // Start at the origin facing the +X direction
    //         new Pose2d(0, 0, new Rotation2d(0)),
    //         // Pass through these two interior waypoints, making an 's' curve path
    //         List.of(new Translation2d(0, 0), new Translation2d(0, 0)),
    //         // End 3 meters straight ahead of where we started, facing forward
    //         new Pose2d(3, 0, new Rotation2d(0)),
    //         config);
    // MecanumControllerCommand mecanumControllerCommand =
    //     new MecanumControllerCommand(
    //         exampleTrajectory,
    //         m_robotDrive::getPose,
    //         DriveConstants.kFeedforward,
    //         DriveConstants.kDriveKinematics,
    //         // Position contollers
    //         new PIDController(AutoConstants.kPXController, 0, 0),
    //         new PIDController(AutoConstants.kPYController, 0, 0),
    //         new ProfiledPIDController(
    //             AutoConstants.kPThetaController, 0, 0,
    // AutoConstants.kThetaControllerConstraints),
    //         // Needed for normalizing wheel speeds
    //         AutoConstants.kMaxSpeedMetersPerSecond,
    //         // Velocity PID's
    //         new PIDController(DriveConstants.kPFrontLeftVel, 0, 0),
    //         new PIDController(DriveConstants.kPRearLeftVel, 0, 0),
    //         new PIDController(DriveConstants.kPFrontRightVel, 0, 0),
    //         new PIDController(DriveConstants.kPRearRightVel, 0, 0),
    //         m_robotDrive::getCurrentWheelSpeeds,
    //         m_robotDrive::setDriveMotorControllersVolts, // Consumer for the output motor
    // voltages
    //         m_robotDrive);
    // // Reset odometry to the starting pose of the trajectory.
    // m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // // Run path following command, then stop at the end.
    // return mecanumControllerCommand.andThen(() -> m_robotDrive.drive(0, 0, 0, false));
  }

  /**
   * Use this to get the PDP for data logging.
   *
   * @return The PowerDistribution module.
   */
  public PowerDistribution getPdp() {
    return this.pdp;
  }
}
