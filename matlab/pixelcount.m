function [bc_vector, img] = pixelcount(img, positions)
% count the black and white pixels using the bresenham line algorithm

nextRow = positions(1); %startposx;
nextCol = positions(2); %startposy;
endRow = positions(3); %endposx;
endCol = positions(4); %endposy;

% variables for pixelcount
pixcount = 0;
currentcolor = img(nextRow, nextCol);
bc_vector = [];

% variables for the line algorithm
deltaRow = endRow - nextRow;
deltaCol = endCol - nextCol;
stepCol = 0;
stepRow = 0;
currentStep = 0;
fraction = 0;

% line algo init
if deltaRow<0
    stepRow = -1;
else
    stepRow = 1;
end
if deltaCol<0
    stepCol = -1;
else
    stepCol = 1;
end
deltaRow = abs(deltaRow*2);
deltaCol = abs(deltaCol*2);

currentStep = currentStep + 1;

if deltaCol>deltaRow
    fraction = deltaRow*2-deltaCol;
    while nextCol ~= endCol
        if fraction >=0
            nextRow = nextRow + stepRow;
            fraction = fraction - deltaCol;
        end
        nextCol = nextCol + stepCol;
        fraction = fraction + deltaRow;
        currentStep = currentStep + 1;
        %pixcount
        if(img(nextRow, nextCol) == currentcolor) 
            pixcount = pixcount + 1;
        else
            bc_vector = [bc_vector, pixcount];
            pixcount = 1;
            currentcolor = img(nextRow, nextCol);
        end
        % set it white to see the scanlines 
        % TODO: remove this line in the final version
        img(nextRow-1, nextCol) = 1;
    end
    bc_vector = [bc_vector, pixcount];
else
    fraction = deltaCol*2-deltaRow;
    while nextRow ~= endRow
        if fraction>=0
            nextCol = nextCol + stepCol;
            fraction = fraction - deltaRow;
        end
        nextRow = nextRow + stepRow;
        fraction = fraction + deltaCol;
        currentStep = currentStep + 1;
        %pixcount
        if(img(nextRow, nextCol) == currentcolor) 
            pixcount = pixcount + 1;
        else
            bc_vector = [bc_vector, pixcount];
            pixcount = 1;
            currentcolor = img(nextRow, nextCol);
        end
        % set it white to see the scanlines 
        % TODO: remove this line in the final version
        img(nextRow-1, nextCol) = 1;
    end
    bc_vector = [bc_vector, pixcount];
end
