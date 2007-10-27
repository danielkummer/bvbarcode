function [ res_vec1 ] = decode(input_vec)

if length(input_vec)>5 % besserer Wert?
    res_vec1 = [];
    [unused, len] = size(input_vec);

    start_vec = input_vec(1:4);
    stop_vec  = input_vec(len-3:len);

    step = 1;


    %kodiersymbole
    % 1     weiss dünn
    % 2     schwarz dünn
    % 3     weiss dick
    % 4     schwarz dick


    if( all(start_vec == [2,1,2,1]) && all(stop_vec(2:4) == [4,1,2]) )
    %if(start_vec(1)==2 && start_vec(2)==1 && start_vec(3)==2 && start_vec(4)==1 && stop_vec(2)==4 && stop_vec(3)==1 && stop_vec(4)==2)
        loop_start = 5;
        loop_end   = len-4;
    elseif( all(start_vec(1:3) == [2,1,4]) && all(stop_vec == [1,2,1,2]) ) %codevektor verkehrt
    %elseif(start_vec(1)==2 && start_vec(2)==1 && start_vec(3)==4 && stop_vec(1)==1 && stop_vec(2)==2 && stop_vec(3)==1 && stop_vec(4)==2)
        step = -1;
        loop_start = len-4;
        loop_end   = 4;
    else
        res_vec1 = [-1];
    end
    
    %start-stop erkannt und vektor hat gerade Anzahl stellen
    if( ~any(res_vec1) && (mod(len-7, 2) == 0) )

        w_number = [];
        b_number = [];
        step_count = 0;

        for i=loop_start:(step*2):loop_end
                b_number = [b_number, input_vec(i) - 1];
                w_number = [w_number, input_vec(i+step)];
                if(step_count == 4)
                    res_vec1 = [res_vec1, codelookup(b_number)];
                    res_vec1 = [res_vec1, codelookup(w_number)];
                    w_number = [];
                    b_number = []; 
                    step_count = 0;
                else
                    step_count = step_count + 1;
                end
        end

    end 
else
    res_vec1 = [-2];
end
