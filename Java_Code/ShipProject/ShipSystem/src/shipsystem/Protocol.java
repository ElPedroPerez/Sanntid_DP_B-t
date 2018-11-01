package shipsystem;

/**
 * Protocol class. values of the protocol used for communication
 *
 * @author Eivind Fugledal
 */
public enum Protocol
{

    CONTROLS(0),
    LEFT_MOTOR_SPEED(1),
    RIGHT_MOTOR_SPEED(2),
    COMMANDS(3),
    SENSITIVITY(4),
    REQUEST_FEEDBACK(5),
    LEFT_THRUSTER_SPEED(6),
    RIGHT_THRUSTER_SPEED(7);

    private int value;

    private Protocol(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }

    public enum controls
    {
        STOP(0),
        FORWARD(1),
        REVERSE(2),
        LEFT(3),
        RIGHT(4),
        LEFT_THRUSTER(6),
        RIGHT_THRUSTER(7);

        private int value;

        private controls(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    public enum commands
    {
        LEFT_SERVO(0),
        RIGHT_SERVO(1),
        AUTO_MANUAL(2),
        START(3);

        private int value;

        private commands(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }
}
