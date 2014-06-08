function [ I, u, old_e ] = PID( e, old_e, I, Kp, Kd, Ki, dt)
%PID Control the speed of car
D = (e-old_e)/dt;
I = I+e*dt;
u = Kp*e + Kd*D+Ki*I;
old_e = e;
end

