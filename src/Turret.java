import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;
import lejos.hardware.motor.Motor;

public class Turret
{
	protected double direction=0;

	public void run()
	{
		try {
			ServerSocket ss = new ServerSocket(1337);
			Socket s = ss.accept();
			ss.close();
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			Motor.A.setSpeed(120);
			Motor.D.setSpeed(60);

			while(true)
			{
				double[] xyz = (double[]) ois.readObject();

				Motor.A.rotateTo((int)(40*xyz[0]));
				System.out.println(xyz[0]);

				if (Math.abs(this.direction - xyz[0]) < 0.01)
					Motor.D.backward();
				else
					Motor.D.flt();

				this.direction=xyz[0];
			}
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] a) {

		int button = 2; //Button.waitForAnyPress();
		// center = 2
		// up = 1
		
		while (button==2) {
			System.out.println("Hello humans.");
			new Turret().run();
			Motor.A.rotateTo(0);
			Motor.A.flt();
			Motor.D.flt();
			System.out.println("PRESS MID");
			button = Button.waitForAnyPress();
		}
	}
}