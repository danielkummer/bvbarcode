function [bc_vector, img] = pixelcount(img, positions)
%function [bc_vector, img] = pixelcount(img, startposx, startposy, endposx, endposy)

% %Anzahl Pixel
% bc_vector = [];
% pixcount = 0;
% currentcolor = img(startpos);
% for i=startpos(2):endpos(2)
%     if(img(startpos(1),i) == currentcolor) 
%         pixcount = pixcount + 1;
%     else
%         bc_vector = [bc_vector, pixcount];
%         pixcount = 1;
%         currentcolor = img(startpos(1), i);
%     end
% end 
% bc_vector = [bc_vector, pixcount];

% unpack positions

nextRow = positions(1); %startposx;
nextCol = positions(2); %startposy;
endRow = positions(3); %endposx;
endCol = positions(4); %endposy;

% vars für pixelcount
pixcount = 0;
currentcolor = img(nextRow, nextCol);
bc_vector = [];

% vars für line algo
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
        % set it white to see TODO: remove this line
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
        % set it white to see TODO: remove this line
        img(nextRow-1, nextCol) = 1;
    end
    bc_vector = [bc_vector, pixcount];
end
            



% def bresenham_line_Algo(pos, endPos): # None
%     """
%     Bresehnhams line algorithmus.
%     This function prints only cooridnates of line.
%     """
%     nextCol, nextRow = pos
%     endCol, endRow = endPos
%     deltaRow = endRow-nextRow
%     deltaCol = endCol-nextCol
%     stepCol = 0
%     stepRow = 0
%     currentStep = 0
%     fraction = 0
%     
%     if deltaRow<0: stepRow = -1 
%     else: stepRow = 1
%     if deltaCol<0: stepCol = -1
%     else: stepCol = 1
%     deltaRow = abs(deltaRow*2)
%     deltaCol = abs(deltaCol*2)
%     
%     currentStep+=1
%     
%     if deltaCol>deltaRow:
%         fraction = deltaRow*2-deltaCol
%         while nextCol!=endCol:
%             if fraction>=0:
%                 j=1
%                 nextRow += stepRow
%                 fraction -= deltaCol
%             nextCol += stepCol
%             fraction += deltaRow
%             currentStep += 1
%             # current pos is nextCol,nextRow
%             print nextCol, nextRow
%             points.append((1,j)) # current pos is nextCol,nextRow
%             j=0
%             
%     else:
%         fraction = deltaCol*2-deltaRow
%         while nextRow!=endRow:
%             if fraction>=0:
%                 j=1
%                 nextCol += stepCol
%                 fraction -= deltaRow
%             nextRow += stepRow
%             fraction += deltaCol
%             currentStep += 1
%             # current pos is nextCol,nextRow
%             print nextCol, nextRow
%             points.append((j,1)) # current pos is nextCol,nextRow
%             j=0
%             
        

