# cs1

[![Discord](https://img.shields.io/discord/384870460640329728.svg?logo=discord)](https://discord.gg/G2kxrnU)

[**View scripts**](https://github.com/RuneStar/cs1/blob/master/scripts.txt)

ClientScript1 (cs1) is a simple client-side scripting language used in Old School RuneScape to dynamically update interfaces.
Used primarily until late 2006 when it was superseded by ClientScript2 (cs2).
Most content was migrated to cs2 but some remains as cs1.
The real source code syntax is unknown.
Compiles to a custom bytecode format and is interpreted by the client.

Each interface component can optionally have an associated script.
Every time the component is drawn the script is executed and either `true` or `false` is returned.
Depending on the result the component is able to alternate between two states.