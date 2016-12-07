[X y] = load_dataset("train.csv");

count_pos = [];
count_neg = [];
c_pos = 0;
c_neg = 0;
for i = 1:length(X)
    split = featurize(X{i});
    for k = 1:length(split)
        feature = split{k};
        %%%
        % Fill the 'train' part of the solution here.
        % Note that you can use count_pos and count_neg as 'associative arrays'.
        % The following functions might come handy:
        %
        % isfield(count_pos, 'word')
        % - Returns 1 if 'word' is in count_pos, otherwise returns 0 
        %
        % getfield(count_neg, 'word')
        % - Returns the value from count_pos associated with 'word'.
        %
        % count_pos = setfield(count_pos, 'word', 1)
        % - Returns a new instance of count_pos in which the value for
        %   'word' is set to 1.


    endfor
endfor


[X_t y_t] = load_dataset("test.csv");
tp = 0;
fp = 0;
fn = 0;
tn = 0;

for i = 1:length(X_t)
    p_pos = 1;
    p_neg = 1;

    split = featurize(X_t{i});
    for k = 1:length(split)
        % This variable holds one feature from the split list above
        feature = split{k};
        %%%
        % Fill the 'classify' part of the solution here
        %


    endfor

    % Set this variable to the class that is most probable for this text.
    % Note that the possible values are 'pos' and 'neg'.
    pred = 'pos';

    if pred == 'pos' && y_t{i} == 'pos'
        tp = tp + 1;
    elseif pred == 'neg' && y_t{i} == 'pos'
        fp = fp + 1;
    elseif pred == 'pos' && y_t{i} == 'neg'
        fn = fn + 1;
    elseif pred == 'neg' && y_t{i} == 'neg'
        tn = tn + 1;
    endif
endfor

printf('Accuracy: %f\n', (tp+tn)/length(X_t))
recall = tp / (tp + fn);
precission = tp / (tp + fp);
printf('F1 score: %f\n', 2 * (precission * recall) / (precission + recall))
