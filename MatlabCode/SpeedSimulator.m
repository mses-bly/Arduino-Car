continueSimulation = 1;
Kp = 0.4;
Kd = 0.001;
Ki = 0.0001;
old_e = 0;
I = 0;
u = 0;
currentSpeed = 0;
while continueSimulation == 1
    reference_speed = input('Set new speed: ');
    step = [];
    speeds = [];
    i = 0;
    
    sampleTime = 0.01;
    tic;
    acc = 0;
    leftExtreme = 0.999*reference_speed;
    rightExtreme = reference_speed + (0.01*reference_speed);
    while ~(currentSpeed >= leftExtreme && currentSpeed <= rightExtreme)
        if toc >= sampleTime
           % acc = acc + toc;
           % disp(acc);
            e = reference_speed-currentSpeed;
            [I, u, old_e] = PID(e,old_e,I,Kp,Kd,Ki,0.01);
            currentSpeed = currentSpeed+u;
            if currentSpeed < 0
                currentSpeed = 0;
            end
            disp(currentSpeed);
            step = [step i];
            speeds = [speeds currentSpeed];
            i = i +0.01;
            tic;
        end;
    end;
    figure;
    title('Speed progression');
    xlabel('Step'); % x-axis label
    ylabel('Speeds'); % y-axis label
    plot(step,speeds);
    continueS = input('Continue?(y/n): ','s');
    if continueS ~= 'y'
        continueSimulation = 0;
    end;
end;




