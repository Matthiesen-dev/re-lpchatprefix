package dev.matthiesen.forge.relpchatprefix.text_parser;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.tysontheember.emberstextapi.immersivemessages.api.MarkupParser;
import net.tysontheember.emberstextapi.immersivemessages.api.TextSpan;
import net.tysontheember.emberstextapi.util.StyleUtil;

import java.util.List;

/**
 * An implementation of the MatthiesenLibTextParser interface for the Embers mod on the NeoForge platform.
 */
public class MatthiesenLibEmbersTextParserNeoForge implements MatthiesenLibTextParser {
    /**
     * Default constructor for the MatthiesenLibEmbersTextParserNeoForge class.
     */
    public MatthiesenLibEmbersTextParserNeoForge() {}

    @Override
    public String getType() {
        return MatthiesenLibBuiltInTextParsers.EMBER.getName();
    }

    @Override
    public Component parse(String text) {
        if (MatthiesenLibApi.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            List<TextSpan> spans = MarkupParser.parse(text);
            MutableComponent result = Component.empty();
            for (TextSpan span : spans) {
                // applyTextSpanFormatting handles bold/italic/effects but intentionally skips color
                Style style = StyleUtil.applyTextSpanFormatting(Style.EMPTY, span);
                if (span.getColor() != null) {
                    style = style.withColor(span.getColor());
                }
                result.append(Component.literal(span.getContent()).withStyle(style));
            }
            return result;
        } else {
            MatthiesenLibApiConstants.createErrorLog("Attempted to parse text with the 'ember' parser, but the Embers mod is not loaded, Falling back to 'vanilla' parser");
            return MatthiesenLibTextParserManager.VANILLA_PARSER.parse(text);
        }
    }
}
