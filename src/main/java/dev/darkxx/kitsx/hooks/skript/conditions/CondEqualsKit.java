/*
 * This file is part of KitsX
 *
 * KitsX
 * Copyright (c) 2024 XyrisPlugins Team
 *
 * KitsX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KitsX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package dev.darkxx.kitsx.hooks.skript.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.darkxx.kitsx.api.events.KitLoadEvent;
import dev.darkxx.kitsx.api.events.KitSaveEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public class CondEqualsKit extends Condition {

    private Expression<String> kitNameExpr;

    @Override
    public boolean check(@NotNull Event event) {
        if (kitNameExpr == null) {
            return isNegated();
        }
        String actualKitName = kitNameExpr.getSingle(event);
        if (actualKitName == null) {
            return isNegated();
        }
        String expectedKitName = null;
        if (event instanceof KitLoadEvent) {
            expectedKitName = ((KitLoadEvent) event).getKitName();
        } else if (event instanceof KitSaveEvent) {
            expectedKitName = ((KitSaveEvent) event).getKitName();
        }
        if (expectedKitName == null) {
            return isNegated();
        }
        return actualKitName.equalsIgnoreCase(expectedKitName) ^ isNegated();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (exprs.length == 0) {
            return false;
        }
        kitNameExpr = (Expression<String>) exprs[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(Event e, boolean debug) {
        return (isNegated() ? "kit isn't equal to " : "kit is equal to ") + kitNameExpr.toString(e, debug);
    }
}
