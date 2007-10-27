function [ number ] = codelookup( input )

N = 1;
W = 3;
%input
if(all(input == [N,N,W,W,N]))
    number = 0;
    return
elseif(all(input == [W,N,N,N,W]))
    number = 1;
    return
elseif(all(input == [N,W,N,N,W]))
    number = 2;
    return
elseif(all(input == [W,W,N,N,N]))
    number = 3;
    return
elseif(all(input == [N,N,W,N,W]))
    number = 4;
    return
elseif(all(input == [W,N,W,N,N]))
    number = 5;
    return
elseif(all(input == [N,W,W,N,N]))
    number = 6;
    return
elseif(all(input == [N,N,N,W,W]))
    number = 7;
    return
elseif(all(input == [W,N,N,W,N]))
    number = 8;
    return
elseif(all(input == [N,W,N,W,N]))
    number = 9;
    return
else
    number = -3
end
    