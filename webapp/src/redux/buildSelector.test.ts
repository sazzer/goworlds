import {buildSelector} from "./buildSelector";

it('Builds a selector to get a simple value', () => {
    const selector = buildSelector(['a', 'b']);

    const selected = selector({
        a: {
            b: 123
        }
    });

    expect(selected).toEqual(123);
});

it('Builds a selector to transform a value', () => {
    const selector = buildSelector(['a', 'b'], num => num * 2);

    const selected = selector({
        a: {
            b: 123
        }
    });

    expect(selected).toEqual(246);
});
