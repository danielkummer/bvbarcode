function [res_vec] = get_code(input_vec, th)

res_vec = [];
[unused, len] = size(input_vec);

if len>1
    max_small = input_vec(2);
    len = len - 2; %temp_vec ist kleiner

    color = 1;  %anfangen mit schwarz

    %kodiersymbole
    % 1     weiss dünn
    % 2     schwarz dünn
    % 3     weiss dick
    % 4     schwarz dick

    for i=2:len+1
        if(input_vec(i) < max_small * th) %schmaler balken
            if(input_vec(i) > max_small)
                max_small = input_vec(i);
            end
            res_vec = [res_vec, color+1];
        else %breiter balken
            res_vec = [res_vec, color+3];
        end
       color = xor(color,1);
    end
end