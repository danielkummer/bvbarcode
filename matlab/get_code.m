function [res_vec] = get_code(input_vec, th)

res_vec = [];
[unused, len] = size(input_vec);

if len>1
    max_small = input_vec(2);
    % temp_vec is smaller because the first and the last entrys don't belong
    % to the code
    len = len - 2; 

    % start with black
    color = 1;  

    % coding symbols
    % 1     white narrow
    % 2     black narrow
    % 3     white wide
    % 4     black wide

    for i=2:len+1
        if(input_vec(i) < max_small * th) % narrow bar
            if(input_vec(i) > max_small)
                max_small = input_vec(i);
            end
            res_vec = [res_vec, color+1];
        else % wide bar
            res_vec = [res_vec, color+3];
        end
       color = xor(color,1); % change color to white
    end
end