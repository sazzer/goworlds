declare module 'uri-template' {
    /**
     * Interface representing a Parsed URI from the URI Template Parser
     */
    export interface ParsedUri {
        /** Expand the parsed URI into a real one with the given binds */
        expand: (binds: object) => string,
    }

    /**
     * Parse a URI Template into a Parsed URI
     * @param input the input to parse
     * @return the parsed URI
     */
    export function parse(input: string) : ParsedUri;
}
